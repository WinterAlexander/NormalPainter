package net.jumpai.util.gfx;

import com.badlogic.gdx.Gdx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static com.badlogic.gdx.graphics.GL20.GL_MAX_TEXTURE_IMAGE_UNITS;

/**
 * Utility class for OpenGL related operations. Not thread safe and can only be
 * called from the OpenGL context thread
 * <p>
 * Created on 2020-09-18.
 *
 * @author Alexander Winter
 */
public class GLUtil
{
	private static int MAX_TEXTURE_IMAGE_UNITS = -1;

	public static int getMaxTextureImageUnits()
	{
		if(MAX_TEXTURE_IMAGE_UNITS != -1)
			return MAX_TEXTURE_IMAGE_UNITS;

		IntBuffer buffer = ByteBuffer.allocateDirect(64)
				.order(ByteOrder.nativeOrder())
				.asIntBuffer();
		Gdx.gl.glGetIntegerv(GL_MAX_TEXTURE_IMAGE_UNITS, buffer);
		return MAX_TEXTURE_IMAGE_UNITS = buffer.get();
	}
}
