package net.jumpai.util.gfx;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

/**
 * FrameBuffer that can dispose itself without disposing its texture
 * <p>
 * Created on 2018-04-08.
 *
 * @author Alexander Winter
 */
public class YieldingFrameBuffer extends FrameBuffer
{
	public YieldingFrameBuffer(Pixmap.Format format, int width, int height, boolean hasDepth)
	{
		super(format, width, height, hasDepth);
	}

	@Override
	protected void disposeColorTexture(Texture t) {}
}
