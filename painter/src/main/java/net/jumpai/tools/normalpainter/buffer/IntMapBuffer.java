package net.jumpai.tools.normalpainter.buffer;

import com.badlogic.gdx.utils.IntIntMap;

/**
 * {@link PixmapBuffer} backed by an {@link IntIntMap}
 * <p>
 * Created on 2020-12-23.
 *
 * @author Alexander Winter
 */
public class IntMapBuffer extends IntIntMap implements PixmapBuffer
{
	private int color = 0x00000000;

	private int width, height;

	public IntMapBuffer(int width, int height)
	{
		super(width * height / 16);
		this.width = width;
		this.height = height;
	}

	@Override
	public void copy(PixmapBuffer pixmap)
	{
		clear();
		if(pixmap instanceof IntMapBuffer)
			putAll((IntMapBuffer)pixmap);
		else
			for(int x = 0; x < pixmap.getWidth(); x++)
				for(int y = 0; y < pixmap.getHeight(); y++)
					drawPixel(x, y, pixmap.getPixel(x, y));
	}

	@Override
	public int getPixel(int x, int y)
	{
		return get(key(x, y), 0x00000000);
	}

	@Override
	public void setColor(int color)
	{
		this.color = color;
	}

	@Override
	public void drawPixel(int x, int y, int color)
	{
		put(key(x, y), color);
	}

	@Override
	public void drawPixel(int x, int y)
	{
		drawPixel(x, y, color);
	}

	private int key(int x, int y)
	{
		return (x & 0xFFFF) | ((y & 0xFFFF) << 16);
	}

	@Override
	public int getWidth()
	{
		return width;
	}

	@Override
	public int getHeight()
	{
		return height;
	}
}
