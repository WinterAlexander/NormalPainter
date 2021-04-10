package net.jumpai.render.lighting;

/**
 * A way of rendering light in the shader of a {@link LightingBatch}
 * <p>
 * Created on 2020-09-28.
 *
 * @author Alexander Winter
 */
public enum LightRenderMode
{
	OFF(),
	STANDARD(),
	CELSHADED(),
	CAPPED(),

	;
}
