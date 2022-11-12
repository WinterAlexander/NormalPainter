package com.normalpainter.app;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.normalpainter.app.jpun.JPenListener;
import com.normalpainter.app.jpun.JPun;
import com.normalpainter.util.controller.ControllerAdapter;
import com.normalpainter.util.math.MathUtil;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.normalpainter.app.NormalPainterScreen.WORLD_HEIGHT;
import static com.normalpainter.app.NormalPainterScreen.WORLD_WIDTH;
import static com.normalpainter.util.Validation.ensureNotNull;
import static com.normalpainter.util.math.MathUtil.pow2;

/**
 * Part of the software responsible for selecting a normal map
 * <p>
 * Created on 2020-12-21.
 *
 * @author Alexander Winter
 */
public class ColorComponent implements ControllerAdapter, JPenListener
{
	public JPun jPenWrapper;

	private NormalPainterScreen screen;

	public boolean disableStickOrPen = false;
	public float normalRotateSpeed = 1f;

	public float maxTilt = 20f;
	public float minRadius = 0f, maxRadius = 1f, radiusStep = 0f;
	public boolean invertPinning = false;

	public boolean normalRelToPath = false;
	public float angle = 90f;

	public float numpadIntensity = 0.75f;

	public boolean invert = false;

	public float lastTiltX = 0f, lastTiltY = 0f;

	/**
	 * True when a pinned point has been set defining the color direction of every pixel
	 */
	public boolean pinned = false;
	public final Vector2 pinnedPoint = new Vector2();

	/**
	 * True when a distance shape has been set defining the color intensity of every pixel
	 */
	public boolean distanceShapeSet = false;
	/**
	 * Radii of every possible direction for the distance shape (precision is 0.1 degree)
	 */
	public final float[] radii = new float[360 * 10];

	public boolean joystickSwapXY = false;
	public boolean joystickInvertX = false;
	public boolean joystickInvertY = false;
	public float joystickRadius = 1f;

	public final Vector3 mousePos = new Vector3();

	public final Vector2 axisPos = new Vector2();
	public final Vector3 normalDir = new Vector3(0f, 0f, 1f);

	public ColorComponent(NormalPainterScreen screen)
	{
		ensureNotNull(screen, "screen");
		this.screen = screen;

		jPenWrapper = new JPun();
		jPenWrapper.addListener(this);
		Controllers.addListener(this);
	}

	public void mouseMoved(int screenX, int screenY)
	{
		if(!pinned && !normalRelToPath)
			return;

		mousePos.set(screenX, screenY, 0f);

		screen.camera.unproject(mousePos,
				screen.stage.getViewport().getScreenX(),
				screen.stage.getViewport().getScreenY(),
				screen.stage.getViewport().getScreenWidth(),
				screen.stage.getViewport().getScreenHeight());


		float len = axisPos.len();

		axisPos.set(mousePos.x, mousePos.y).sub(normalRelToPath ? screen.lastDraw : pinnedPoint);

		if(distanceShapeSet)
		{
			len = axisPos.len() / radii[Math.round(axisPos.angle() * 10f) % (360 * 10)];

			if(len > 1.01f)
				len = 0f;
			else if(len > 1.0f)
				len = (0.01f - (len - 1f)) * 100f; // nice smoothing
		}

		if(normalRelToPath)
			axisPos.rotate(-angle);
		else if(invertPinning)
			axisPos.scl(-1f);

		axisPos.nor().scl(len);
		updateNormalDir();
	}

	public boolean updateAxis(int screenX, int screenY, boolean evenIfOut)
	{
		mousePos.set(screenX, screenY, 0f);

		screen.camera.unproject(mousePos,
				screen.stage.getViewport().getScreenX(),
				screen.stage.getViewport().getScreenY(),
				screen.stage.getViewport().getScreenWidth(),
				screen.stage.getViewport().getScreenHeight());

		float rangeSize = 2000f * screen.camera.zoom;
		float rangeStartX = screen.camera.position.x + WORLD_WIDTH / 2f * screen.camera.zoom - rangeSize * 1.05f;
		float rangeStartY = screen.camera.position.y + WORLD_HEIGHT / 2f * screen.camera.zoom - rangeSize * 1.05f;

		boolean inBox = MathUtil.inBox(mousePos.x, mousePos.y, rangeStartX, rangeStartY, rangeSize);

		if(!inBox && !evenIfOut)
			return false;

		axisPos.set(mousePos.x - rangeStartX, mousePos.y - rangeStartY).scl(2f / rangeSize).sub(1f, 1f);
		updateNormalDir();
		return inBox;
	}

	public void setColor()
	{
		screen.painter.color.r = (normalDir.x + 1f) / 2f;
		screen.painter.color.g = (normalDir.y + 1f) / 2f;
		screen.painter.color.b = (normalDir.z + 1f) / 2f;
		screen.painter.color.a = 1f;
	}

	public void updateNormalDir()
	{
		if(axisPos.len2() > pow2(maxRadius))
			axisPos.nor().scl(maxRadius);
		else if(axisPos.len2() < pow2(minRadius))
			axisPos.nor().scl(minRadius);

		if(radiusStep > 0f)
		{
			float len = axisPos.len();
			axisPos.scl(Math.round(len / radiusStep) * radiusStep / len);
		}

		normalDir.x = axisPos.x * (invert ? -1f : 1f);
		normalDir.y = axisPos.y * (invert ? -1f : 1f);
		normalDir.z = (float)Math.sqrt(Math.max(0f, 1f - pow2(normalDir.x) - pow2(normalDir.y)));
		normalDir.nor();
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value)
	{
		if(screen.app.getScreen() != screen || disableStickOrPen)
			return false;

		if(axisCode % 2 == (joystickSwapXY ? 1 : 0))
			axisPos.x = value * (joystickInvertX ? -1f : 1f) / joystickRadius;
		else
			axisPos.y = -value * (joystickInvertY ? -1f : 1f) / joystickRadius;

		updateNormalDir();

		return true;
	}

	@Override
	public void pressureChanged(float pressure) {}

	@Override
	public void tiltChanged(float x, float y)
	{
		if(screen.app.getScreen() != screen || disableStickOrPen)
			return;

		y = -y;

		lastTiltX = x;
		lastTiltY = y;

		axisPos.set(x, y).scl(1f / maxTilt);
		axisPos.x = clamp(axisPos.x, -1f, 1f);
		axisPos.y = clamp(axisPos.y, -1f, 1f);

		updateNormalDir();
	}

	public void draw()
	{
		float rangeSize = 2000f * screen.camera.zoom;
		float rangeStartX = screen.camera.position.x + WORLD_WIDTH / 2f * screen.camera.zoom - rangeSize * 1.05f;
		float rangeStartY = screen.camera.position.y + WORLD_HEIGHT / 2f * screen.camera.zoom - rangeSize * 1.05f;

		screen.batch.draw(screen.normalRange, rangeStartX, rangeStartY, rangeSize / 2f, rangeSize / 2f, rangeSize, rangeSize, invert ? -1f : 1f, invert ? -1f : 1f, 0f);

		float cursorSize = 250f * screen.camera.zoom;

		screen.batch.draw(screen.normalCursor,
				rangeStartX + (axisPos.x + 1f) / 2f * rangeSize - cursorSize / 2f,
				rangeStartY + (axisPos.y + 1f) / 2f * rangeSize - cursorSize / 2f,
				cursorSize,
				cursorSize);

		float colorSize = 500f * screen.camera.zoom;

		screen.batch.setColor(screen.painter.color);
		screen.batch.draw(screen.whitePixel,
				screen.camera.position.x + WORLD_WIDTH / 2f * screen.camera.zoom - rangeSize * 1.05f,
				screen.camera.position.y + WORLD_HEIGHT / 2f * screen.camera.zoom - rangeSize * 1.05f - colorSize * 1.05f,
				rangeSize,
				colorSize);
	}
}
