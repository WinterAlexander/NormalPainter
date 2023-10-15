package com.normalpainter.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.normalpainter.app.buffer.GdxPixmap;
import com.normalpainter.app.buffer.PixmapBuffer;
import com.normalpainter.app.buffer.RangedGdxBuffer;
import com.normalpainter.app.jpun.JPenListener;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.normalpainter.app.NormalPainterScreen.WORLD_PERFECT_SCALE;
import static com.winteralexander.gdx.utils.Validation.ensureNotNull;
import static java.lang.Math.*;

/**
 * Part of the software responsible for painting the pixmap
 * <p>
 * Created on 2020-12-20.
 *
 * @author Alexander Winter
 */
public class PaintComponent implements JPenListener {
	public static final float[][] GAUSSIAN_BLUR_3_3_KERNEL = new float[][] { { 1f / 16f, 1f / 8f,
			1f / 16f }, { 1f / 8f, 1f / 4f, 1f / 8f }, { 1f / 16f, 1f / 8f, 1f / 16f }, };

	public NormalPainterScreen screen;

	public GdxPixmap pixmap;
	public RangedGdxBuffer preview;

	public RangedGdxBuffer maskPreview;
	public RangedGdxBuffer brushBuffer;
	public RangedGdxBuffer maskBuffer;
	public GdxPixmap mask;

	public RangedGdxBuffer maskBufferBackup;
	public GdxPixmap backup;

	public float brushOpacity = 1f;
	public int brushSize = 5;
	public boolean enablePressure = false;
	public float maxPressure = 0.85f;
	public float hardness = 0.75f;
	public DrawMode drawMode = DrawMode.Normal;
	public float equalizeWeight = 0.5f;
	public boolean maskMultiply = true;

	public float pressure = 1f;

	public boolean uncommittedMaskChanges = false;

	private int startX = -1, startY = -1, endX = -1, endY = -1;

	public final Color color = new Color(), otherColor = new Color(), tmpColor = new Color(),
			tmpColor2 = new Color(), bufferColor = new Color();

	private final Vector3 tmpVec3 = new Vector3();

	private final Vector2 tmpLine = new Vector2();
	private final Vector2 tmpLine2 = new Vector2();

	public PaintComponent(NormalPainterScreen screen) {
		ensureNotNull(screen, "screen");
		this.screen = screen;
	}

	public boolean fillLine(int prevPixX, int prevPixY, int pixelX, int pixelY) {
		int radius = Math.round((enablePressure ? pressure : 1f) * brushSize);

		int minX = min(prevPixX, pixelX);
		int maxX = max(prevPixX, pixelX);
		int minY = min(prevPixY, pixelY);
		int maxY = max(prevPixY, pixelY);

		if(minX - radius + 1 >= pixmap.getWidth() || minY - radius + 1 >= pixmap.getHeight() || maxX + radius <= 0 || maxY + radius <= 0)
			return false;

		tmpLine.set(pixelX - prevPixX, pixelY - prevPixY);

		float lineLen = tmpLine.len();

		if(lineLen <= 0f)
			return fillCircle(pixelX, pixelY);

		tmpLine.scl(1f / lineLen);

		for(int x = minX - radius + 1; x < maxX + radius; x++)
			for(int y = minY - radius + 1; y < maxY + radius; y++) {
				tmpLine2.set(x - prevPixX, y - prevPixY);

				float dot = tmpLine.dot(tmpLine2);

				float rad;

				if(dot < 0f)
					rad = tmpLine2.len();
				else if(dot > lineLen)
					rad = tmpLine2.set(x - pixelX, y - pixelY).len();
				else {
					tmpLine2.mulAdd(tmpLine, -dot);
					rad = tmpLine2.len();
				}

				if(rad > radius)
					continue;

				drawPixel(x, y, rad, radius);
			}

		_fillCircle(prevPixX, prevPixY, radius);
		_fillCircle(pixelX, pixelY, radius);

		screen.colorPicker.mouseMoved(Gdx.input.getX(), Gdx.input.getY());

		if(startX < 0) {
			startX = max(0, minX - radius + 1);
			startY = max(0, minY - radius + 1);
			endX = min(pixmap.getWidth(), maxX + radius);
			endY = min(pixmap.getHeight(), maxY + radius);
		} else {
			if(minX - radius + 1 < startX)
				startX = max(0, minX - radius + 1);

			if(minY - radius + 1 < startY)
				startY = max(0, minY - radius + 1);

			if(maxX + radius > endX)
				endX = min(pixmap.getWidth(), maxX + radius);

			if(maxY + radius > endY)
				endY = min(pixmap.getHeight(), maxY + radius);
		}

		return true;
	}

	private boolean _fillCircle(int pixelX, int pixelY, int radius) {
		if(pixelX - radius + 1 >= pixmap.getWidth() || pixelY - radius + 1 >= pixmap.getHeight() || pixelX + radius <= 0 || pixelY + radius <= 0)
			return false;

		for(int i = -radius + 1; i < radius; i++) {
			for(int j = -radius + 1; j < radius; j++) {
				int r2 = i * i + j * j;
				if(r2 > radius * radius)
					continue;

				int x = pixelX + i;
				int y = pixelY + j;

				float rad = (float)Math.sqrt(r2);
				drawPixel(x, y, rad, radius);
			}
		}

		return true;
	}

	public boolean fillCircle(int pixelX, int pixelY) {
		int radius = Math.round(pressure * brushSize);

		if(!_fillCircle(pixelX, pixelY, radius))
			return false;

		if(screen.colorPicker.pinned || screen.colorPicker.normalRelToPath)
			screen.colorPicker.mouseMoved(Gdx.input.getX(), Gdx.input.getY());

		if(startX < 0) {
			startX = max(0, pixelX - radius + 1);
			startY = max(0, pixelY - radius + 1);
			endX = min(pixmap.getWidth(), pixelX + radius);
			endY = min(pixmap.getHeight(), pixelY + radius);
		} else {
			if(pixelX - radius + 1 < startX)
				startX = max(0, pixelX - radius + 1);

			if(pixelY - radius + 1 < startY)
				startY = max(0, pixelY - radius + 1);

			if(pixelX + radius > endX)
				endX = min(pixmap.getWidth(), pixelX + radius);

			if(pixelY + radius > endY)
				endY = min(pixmap.getHeight(), pixelY + radius);
		}

		return true;
	}

	private void drawPixel(int x, int y, float rad, int brushSize) {
		if(maskMultiply)
			uncommittedMaskChanges = true;

		float hardRad = hardness * brushSize;

		float opacity = brushSize == hardRad ? 1f :
				clamp((brushSize - rad) / (brushSize - hardRad), 0f, 1f);

		bufferColor.set(brushBuffer.getPixel(x, y));

		if(screen.colorPicker.pinned || screen.colorPicker.normalRelToPath) {
			float worldX = (x - pixmap.getWidth() / 2f) * WORLD_PERFECT_SCALE;
			float worldY = -(y - pixmap.getHeight() / 2f) * WORLD_PERFECT_SCALE;

			tmpVec3.set(worldX, worldY, 0f);

			screen.camera.project(tmpVec3, screen.stage.getViewport().getScreenX(),
					screen.stage.getViewport().getScreenY(),
					screen.stage.getViewport().getScreenWidth(),
					screen.stage.getViewport().getScreenHeight());

			screen.colorPicker.mouseMoved(Math.round(tmpVec3.x),
					screen.stage.getViewport().getScreenHeight() - Math.round(tmpVec3.y));
			screen.colorPicker.setColor();
		}

		otherColor.set(color);

		float eqW = 1f - (1f - 0.5f) * bufferColor.a;

		otherColor.r = otherColor.r * eqW + bufferColor.r * (1f - eqW);
		otherColor.g = otherColor.g * eqW + bufferColor.g * (1f - eqW);
		otherColor.b = otherColor.b * eqW + bufferColor.b * (1f - eqW);

		otherColor.a = max(opacity, bufferColor.a);

		brushBuffer.setColor(otherColor);
		brushBuffer.drawPixel(x, y);
	}

	public void updatePreview(boolean clean) {
		if(clean)
			preview.fullRange();
		if(clean || startX == -1)
			preview.drawPixmap(pixmap, preview.getMinX(), preview.getMinY(), preview.getMinX(),
					preview.getMinY(), preview.getMaxX() - preview.getMinX(),
					preview.getMaxY() - preview.getMinY());
		else
			preview.drawPixmap(pixmap, startX, startY, startX, startY, endX - startX,
					endY - startY);
		preview.initRange();

		if(maskMultiply) {
			if(clean || startX < 0) {
				maskPreview.copy(maskBuffer);
				renderBuffer(maskPreview);
			} else {
				maskPreview.drawPixmap(maskBuffer, startX, startY, startX, startY, endX - startX,
						endY - startY);
				renderBuffer(maskPreview, startX, startY, endX, endY);
			}
		} else {
			if(clean || startX < 0)
				renderBuffer(preview);
			else
				renderBuffer(preview, startX, startY, endX, endY);
		}

		if(uncommittedMaskChanges) {
			if(clean || startX < 0)
				flushMaskBuffer(maskPreview, preview, false);
			else
				flushMaskBuffer(maskPreview, preview, false, startX, startY, endX, endY);
		}

		startX = -1;
		startY = -1;
		endX = -1;
		endY = -1;
	}

	public void normalizeAll() {
		for(int x = 0; x < pixmap.getWidth(); x++)
			for(int y = 0; y < pixmap.getHeight(); y++) {
				otherColor.set(pixmap.getPixel(x, y));

				normalize(otherColor);

				pixmap.setColor(otherColor);
				pixmap.drawPixel(x, y);
			}

		updatePreview(true);
	}

	public void blur() {
		flushMask();
		backup.copy(pixmap);

		for(int x = 0; x < pixmap.getWidth(); x++)
			for(int y = 0; y < pixmap.getHeight(); y++) {
				getBlurredColor(backup, x, y, GAUSSIAN_BLUR_3_3_KERNEL, otherColor);
				pixmap.setColor(otherColor);
				pixmap.drawPixel(x, y);
			}

		updatePreview(true);
	}

	private void getBlurredColor(GdxPixmap pixmap, int centerX, int centerY, float[][] kernel,
	                             Color out) {

		int kernelSize = kernel.length;

		out.set(0);

		for(int x = centerX - kernelSize / 2; x <= centerX + kernelSize / 2; x++) {
			for(int y = centerY - kernelSize / 2; y <= centerY + kernelSize / 2; y++) {
				int px = x;
				int py = y;

				if(px < 0)
					px = -px + 1;

				if(py < 0)
					py = -py + 1;

				if(px >= pixmap.getWidth())
					px = 2 * pixmap.getWidth() - px - 1;

				if(py >= pixmap.getHeight())
					py = 2 * pixmap.getHeight() - py - 1;

				tmpColor2.set(pixmap.getPixel(px, py));
				tmpColor.set(0.5f, 0.5f, 1.0f, 1.0f);
				tmpColor.mul(1.0f - tmpColor2.a);

				tmpColor.add(tmpColor2);

				tmpColor.mul(kernel[x - centerX + kernelSize / 2][y - centerY + kernelSize / 2]);
				out.add(tmpColor);
			}
		}

	}

	private void renderBuffer(PixmapBuffer pixmap) {
		renderBuffer(pixmap, brushBuffer.getMinX(), brushBuffer.getMinY(), brushBuffer.getMaxX(),
				brushBuffer.getMaxY());
	}

	private void renderBuffer(PixmapBuffer pixmap, int startX, int startY, int endX, int endY) {
		for(int x = startX; x < endX; x++)
			for(int y = startY; y < endY; y++) {
				bufferColor.set(brushBuffer.getPixel(x, y));

				if(bufferColor.a == 0f)
					continue;

				if(drawMode == DrawMode.Normal) {
					otherColor.set(pixmap.getPixel(x, y));

					float eqW = 1f - (1f - equalizeWeight) * otherColor.a;

					float r = bufferColor.r * eqW + otherColor.r * (1f - eqW);
					float g = bufferColor.g * eqW + otherColor.g * (1f - eqW);
					float b = bufferColor.b * eqW + otherColor.b * (1f - eqW);

					float a = bufferColor.a * brushOpacity;

					otherColor.set(r * a + otherColor.r * otherColor.a * (1f - a),
							g * a + otherColor.g * otherColor.a * (1f - a),
							b * a + otherColor.b * otherColor.a * (1f - a),
							a + otherColor.a * (1f - a));

					otherColor.r /= otherColor.a;
					otherColor.g /= otherColor.a;
					otherColor.b /= otherColor.a;

					normalize(otherColor);

					pixmap.setColor(otherColor);
					pixmap.drawPixel(x, y);
				} else if(drawMode == DrawMode.Erase) {
					otherColor.set(pixmap.getPixel(x, y));

					float multAlpha = bufferColor.a * brushOpacity;

					otherColor.a *= 1f - multAlpha;
					pixmap.setColor(otherColor);
					pixmap.drawPixel(x, y);
				} else if(drawMode == DrawMode.Behind) {
					otherColor.set(pixmap.getPixel(x, y));

					float r = bufferColor.r;
					float g = bufferColor.g;
					float b = bufferColor.b;

					float a = bufferColor.a * brushOpacity;

					otherColor.set(r * a * (1f - otherColor.a) + otherColor.r * otherColor.a,
							g * a * (1f - otherColor.a) + otherColor.g * otherColor.a,
							b * a * (1f - otherColor.a) + otherColor.b * otherColor.a,
							a * (1f - otherColor.a) + otherColor.a);

					otherColor.r /= otherColor.a;
					otherColor.g /= otherColor.a;
					otherColor.b /= otherColor.a;

					normalize(otherColor);

					pixmap.setColor(otherColor);
					pixmap.drawPixel(x, y);
				}
			}

	}

	public void endDraw() {
		if(!screen.livePreview)
			updatePreview(false);

		if(maskMultiply)
			renderBuffer(maskBuffer);
		else
			renderBuffer(pixmap);
		brushBuffer.clear();
	}

	public void flushMask() {
		flushMaskBuffer(maskBuffer, pixmap, true);
		uncommittedMaskChanges = false;
	}

	public void flushMaskBuffer(RangedGdxBuffer buffer, Pixmap destination, boolean clear) {
		flushMaskBuffer(buffer, destination, clear, buffer.getMinX(), buffer.getMinY(),
				buffer.getMaxX(), buffer.getMaxY());
	}

	public void flushMaskBuffer(RangedGdxBuffer buffer, Pixmap destination, boolean clear,
	                            int startX, int startY, int endX, int endY) {
		for(int x = startX; x < endX; x++)
			for(int y = startY; y < endY; y++) {
				bufferColor.set(buffer.getPixel(x, y));

				if(bufferColor.a == 0f)
					continue;

				if(mask != null)
					tmpColor.set(mask.getPixel(x, y));
				else
					tmpColor.set(Color.WHITE);

				bufferColor.mul(tmpColor);

				otherColor.set(destination.getPixel(x, y));

				bufferColor.r =
						bufferColor.r * bufferColor.a + otherColor.r * otherColor.a * (1f - bufferColor.a);
				bufferColor.g =
						bufferColor.g * bufferColor.a + otherColor.g * otherColor.a * (1f - bufferColor.a);
				bufferColor.b =
						bufferColor.b * bufferColor.a + otherColor.b * otherColor.a * (1f - bufferColor.a);
				bufferColor.a = bufferColor.a + otherColor.a * (1f - bufferColor.a);

				bufferColor.r /= bufferColor.a;
				bufferColor.g /= bufferColor.a;
				bufferColor.b /= bufferColor.a;

				normalize(bufferColor);

				destination.setColor(bufferColor);
				destination.drawPixel(x, y);
			}

		if(clear)
			buffer.clear();
	}

	private void normalize(Color color) {
		tmpVec3.set((int)(color.r * 255), (int)(color.g * 255), (int)(color.b * 255));
		tmpVec3.scl(1f / 255f);
		tmpVec3.scl(2f).sub(1f);

		if(abs(tmpVec3.len() - 1f) < 2f / 255f)
			return;

		tmpVec3.nor();

		tmpVec3.add(1f).scl(0.5f);

		color.r = tmpVec3.x;
		color.g = tmpVec3.y;
		color.b = tmpVec3.z;
	}

	public void init() {
		screen.buffer = new GdxPixmap(screen.painter.pixmap.getWidth(),
				screen.painter.pixmap.getHeight());
		backup = new GdxPixmap(pixmap.getWidth(), pixmap.getHeight());
		brushBuffer = new RangedGdxBuffer(pixmap.getWidth(), pixmap.getHeight());
		preview = new RangedGdxBuffer(pixmap.getWidth(), pixmap.getHeight());
		maskBuffer = new RangedGdxBuffer(pixmap.getWidth(), pixmap.getHeight());
		maskPreview = new RangedGdxBuffer(pixmap.getWidth(), pixmap.getHeight());
		maskBufferBackup = new RangedGdxBuffer(pixmap.getWidth(), pixmap.getHeight());

		preview.setBlending(Blending.None);
		preview.fullRange();
		backup.copy(pixmap);

		updatePreview(true);
	}

	@Override
	public void pressureChanged(float pressure) {
		this.pressure = pressure / maxPressure;
	}

	@Override
	public void tiltChanged(float x, float y) {}

	public void undo() {
		if(maskMultiply) {
			RangedGdxBuffer tmp = maskBuffer;
			maskBuffer = maskBufferBackup;
			updatePreview(true);

			maskBufferBackup = tmp;
		} else {
			GdxPixmap tmp = pixmap;
			pixmap = backup;
			updatePreview(true);

			backup = tmp;
		}
	}
}
