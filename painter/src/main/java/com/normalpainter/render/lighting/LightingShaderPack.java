package com.normalpainter.render.lighting;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.normalpainter.render.ShaderPack;
import com.normalpainter.util.Validation;

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
		Validation.ensureNotNull(noLightingShader, "noLightingShader");
		Validation.ensureNotNull(lightingShader, "lightingShader");
		Validation.ensureNotNull(colorLightingShader, "colorLightingShader");
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
