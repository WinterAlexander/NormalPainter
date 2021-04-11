package com.normalpainter.app.buffer;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * {@link GdxPixmap} that has an edition range to avoid wasting time on useless operations
 * <p>
 * Created on 2020-12-23.
 *
 * @author Alexander Winter
 */
public class RangedGdxBuffer extends GdxPixmap
{
	private int minX, maxX, minY, maxY;

	public RangedGdxBuffer(int width, int height)
	{
		super(width, height);
		initRange();
	}

	public RangedGdxBuffer(FileHandle file)
	{
		super(file);
		initRange();
	}

	public void initRange()
	{
		minX = getWidth();
		maxX = 0;
		minY = getHeight();
		maxY = 0;
	}

	@Override
	public void clear()
	{
		super.clear();
		initRange();
	}

	@Override
	public void copy(PixmapBuffer pixmap)
	{
		if(pixmap instanceof RangedGdxBuffer)
		{
			clear();

			minX = ((RangedGdxBuffer)pixmap).getMinX();
			maxX = ((RangedGdxBuffer)pixmap).getMaxX();
			minY = ((RangedGdxBuffer)pixmap).getMinY();
			maxY = ((RangedGdxBuffer)pixmap).getMaxY();

			if(maxX - minX < 0 || maxY - minY < 0)
				return;

			drawPixmap((RangedGdxBuffer)pixmap, minX, minY, minX, minY, maxX - minX, maxY - minY);
		}
		else
		{

			super.copy(pixmap);
			fullRange();
		}
	}

	public void fullRange()
	{
		minX = 0;
		maxX = getWidth();
		minY = 0;
		maxY = getHeight();
	}

	@Override
	public void fill()
	{
		super.fill();
		fullRange();
	}

	@Override
	public void drawLine(int x, int y, int x2, int y2)
	{
		super.drawLine(x, y, x2, y2);

		int minLx = min(x, x2);
		int maxLx = max(x, x2);
		int minLy = min(y, y2);
		int maxLy = max(y, y2);

		if(minLx < minX)
			minX = minLx;

		if(minLy < minY)
			minY = minLy;

		if(maxLx > maxX)
			maxX = maxLx;

		if(maxLy > maxY)
			maxY = maxLy;
	}

	@Override
	public void drawRectangle(int x, int y, int width, int height)
	{
		super.drawRectangle(x, y, width, height);

		if(x < minX)
			minX = x;

		if(y < minY)
			minY = y;

		if(x + width > maxX)
			maxX = x + width;

		if(y + height > maxY)
			maxY = y + height;
	}

	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y)
	{
		super.drawPixmap(pixmap, x, y);

		int width = pixmap.getWidth();
		int height = pixmap.getHeight();

		if(x < minX)
			minX = x;

		if(y < minY)
			minY = y;

		if(x + width > maxX)
			maxX = x + width;

		if(y + height > maxY)
			maxY = y + height;
	}

	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcx, int srcy, int srcWidth, int srcHeight)
	{
		super.drawPixmap(pixmap, x, y, srcx, srcy, srcWidth, srcHeight);

		if(x < minX)
			minX = x;

		if(y < minY)
			minY = y;

		if(x + srcWidth > maxX)
			maxX = x + srcWidth;

		if(y + srcHeight > maxY)
			maxY = y + srcHeight;
	}

	@Override
	public void drawPixmap(Pixmap pixmap,
	                       int srcx, int srcy, int srcWidth, int srcHeight,
	                       int dstx, int dsty, int dstWidth, int dstHeight)
	{
		super.drawPixmap(pixmap, srcx, srcy, srcWidth, srcHeight, dstx, dsty, dstWidth, dstHeight);


		if(dstx < minX)
			minX = dstx;

		if(dsty < minY)
			minY = dsty;

		if(dstx + dstWidth > maxX)
			maxX = dstx + dstWidth;

		if(dsty + dstHeight > maxY)
			maxY = dsty + dstHeight;
	}

	@Override
	public void fillRectangle(int x, int y, int width, int height)
	{
		super.fillRectangle(x, y, width, height);

		if(x < minX)
			minX = x;

		if(y < minY)
			minY = y;

		if(x + width > maxX)
			maxX = x + width;

		if(y + height > maxY)
			maxY = y + height;
	}

	@Override
	public void drawCircle(int x, int y, int radius)
	{
		super.drawCircle(x, y, radius);

		x = x - radius - 1;
		y = y - radius - 1;
		int width = 2 * radius + 1;
		int height = 2 * radius + 1;

		if(x < minX)
			minX = x;

		if(y < minY)
			minY = y;

		if(x + width > maxX)
			maxX = x + width;

		if(y + height > maxY)
			maxY = y + height;
	}

	@Override
	public void fillCircle(int x, int y, int radius)
	{
		super.fillCircle(x, y, radius);

		x = x - radius - 1;
		y = y - radius - 1;
		int width = 2 * radius + 1;
		int height = 2 * radius + 1;

		if(x < minX)
			minX = x;

		if(y < minY)
			minY = y;

		if(x + width > maxX)
			maxX = x + width;

		if(y + height > maxY)
			maxY = y + height;
	}

	@Override
	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3)
	{
		super.fillTriangle(x1, y1, x2, y2, x3, y3);

		int minTx = min(x1, min(x2, x3));
		int maxTx = max(x1, max(x2, x3));
		int minTy = min(y1, min(y2, y3));
		int maxTy = max(y1, max(y2, y3));

		if(minTx < minX)
			minX = minTx;

		if(minTy < minY)
			minY = minTy;

		if(maxTx > maxX)
			maxX = maxTx;

		if(maxTy > maxY)
			maxY = maxTy;
	}

	@Override
	public void drawPixel(int x, int y)
	{
		super.drawPixel(x, y);

		if(x < minX)
			minX = x;

		if(y < minY)
			minY = y;

		if(x + 1 > maxX)
			maxX = x + 1;

		if(y + 1 > maxY)
			maxY = y + 1;
	}

	@Override
	public void drawPixel(int x, int y, int color)
	{
		super.drawPixel(x, y, color);

		if(x < minX)
			minX = x;

		if(y < minY)
			minY = y;

		if(x + 1 > maxX)
			maxX = x + 1;

		if(y + 1 > maxY)
			maxY = y + 1;
	}

	public int getMinX()
	{
		return minX;
	}

	public void setMinX(int minX)
	{
		this.minX = minX;
	}

	public int getMaxX()
	{
		return maxX;
	}

	public void setMaxX(int maxX)
	{
		this.maxX = maxX;
	}

	public int getMinY()
	{
		return minY;
	}

	public void setMinY(int minY)
	{
		this.minY = minY;
	}

	public int getMaxY()
	{
		return maxY;
	}

	public void setMaxY(int maxY)
	{
		this.maxY = maxY;
	}
}
