package com.normalpainter.app.buffer;

import com.badlogic.gdx.graphics.Color;

/**
 * Abstract buffer for a pixmap implemented in any way
 * <p>
 * Created on 2020-12-23.
 *
 * @author Alexander Winter
 */
public interface PixmapBuffer
{
	int getPixel(int x, int y);

	void setColor(int color);

	default void setColor(float r, float g, float b, float a) {
		setColor(Color.rgba8888(r, g, b, a));
	}

	default void setColor(Color color) {
		setColor(Color.rgba8888(color));
	}

	void copy(PixmapBuffer pixmap);

	void drawPixel(int x, int y);

	void drawPixel(int x, int y, int color);

	void clear();

	int getWidth();

	int getHeight();
}
