package net.jumpai.render;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Pack of shaders which are meant to have a common effect but each operate
 * under different conditions of a {@link Batch} for wider support
 * <p>
 * Created on 2020-09-13.
 *
 * @author Alexander Winter
 */
public interface ShaderPack
{
	void bind(Batch batch);
}
