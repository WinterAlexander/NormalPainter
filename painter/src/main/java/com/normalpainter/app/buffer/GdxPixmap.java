package com.normalpainter.app.buffer;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.IntIntMap.Entry;

/**
 * {@link PixmapBuffer} implemented from a {@link Pixmap}
 * <p>
 * Created on 2020-12-23.
 *
 * @author Alexander Winter
 */
public class GdxPixmap extends Pixmap implements PixmapBuffer
{
	public GdxPixmap(int width, int height)
	{
		super(width, height, Format.RGBA8888);
		setBlending(Blending.None);
	}

	public GdxPixmap(FileHandle file)
	{
		super(file);
		setBlending(Blending.None);
	}

	@Override
	public void copy(PixmapBuffer pixmap)
	{
		if(pixmap instanceof GdxPixmap)
		{
			drawPixmap((Pixmap)pixmap, 0, 0);
		}
		else if(pixmap instanceof IntMapBuffer)
		{
			clear();
			for(Entry entry : ((IntMapBuffer)pixmap).entries())
			{
				int x = entry.key & 0xFFFF;
				int y = (entry.key >> 16) & 0xFFFF;

				drawPixel(x, y, entry.value);
			}
		}
	}

	@Override
	public void clear()
	{
		setColor(0x00000000);
		fill();
	}
}
