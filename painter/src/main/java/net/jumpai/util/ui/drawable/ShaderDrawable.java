package net.jumpai.util.ui.drawable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Drawable that uses a shader to render itself
 * <p>
 * Created on 2019-12-31.
 *
 * @author Alexander Winter
 */
public class ShaderDrawable extends WrapperDrawable
{
	public ShaderProgram shader;

	public ShaderDrawable(Drawable drawable, ShaderProgram shader)
	{
		super(drawable);
		ensureNotNull(shader, "shader");
		this.shader = shader;
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height)
	{
		ShaderProgram previous = batch.getShader();
		batch.setShader(shader);
		super.draw(batch, x, y, width, height);
		batch.setShader(previous);
	}
}
