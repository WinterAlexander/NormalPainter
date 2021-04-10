package net.jumpai.render.lighting;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.jumpai.render.ShaderPack;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * {@link ShaderPack} for shaders which operate under different lighting
 * settings
 * <p>
 * Created on 2020-09-13.
 *
 * @author Alexander Winter
 */
public class LightingShaderPack implements ShaderPack
{
	private final ShaderProgram noLightingShader, lightingShader, colorLightingShader;

	public LightingShaderPack(ShaderProgram noLightingShader,
	                          ShaderProgram lightingShader)
	{
		this(noLightingShader, lightingShader, lightingShader);
	}

	public LightingShaderPack(ShaderProgram noLightingShader,
	                          ShaderProgram lightingShader,
	                          ShaderProgram colorLightingShader)
	{
		ensureNotNull(noLightingShader, "noLightingShader");
		ensureNotNull(lightingShader, "lightingShader");
		ensureNotNull(colorLightingShader, "colorLightingShader");
		this.noLightingShader = noLightingShader;
		this.lightingShader = lightingShader;
		this.colorLightingShader = colorLightingShader;
	}

	@Override
	public void bind(Batch batch)
	{
		if(batch instanceof LightingBatch)
			if(((LightingBatch)batch).supportsColor())
				((LightingBatch)batch).setLightingShader(colorLightingShader);
			else
				((LightingBatch)batch).setLightingShader(lightingShader);
		else
			batch.setShader(noLightingShader);
	}

	public ShaderProgram getNoLightingShader()
	{
		return noLightingShader;
	}

	public ShaderProgram getLightingShader()
	{
		return lightingShader;
	}

	public ShaderProgram getColorLightingShader()
	{
		return colorLightingShader;
	}
}
