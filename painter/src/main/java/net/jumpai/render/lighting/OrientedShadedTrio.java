package net.jumpai.render.lighting;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import net.jumpai.render.UVTransform;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Just like a {@link ShadedTrio} but with support for preshaded textures in various orientations
 * <p>
 * Created on 2020-12-02.
 *
 * @author Alexander Winter
 */
public class OrientedShadedTrio
{
	public final TextureRegion flat, normal;
	public final ObjectMap<UVTransform, TextureRegion> preshadeds = new ObjectMap<>();

	public OrientedShadedTrio(TextureRegion flat, TextureRegion normal, ObjectMap<UVTransform, TextureRegion> preshadeds)
	{
		ensureNotNull(flat, "flat");
		ensureNotNull(normal, "normal");
		ensureNotNull(preshadeds, "preshadeds");

		this.flat = flat;
		this.normal = normal;
		this.preshadeds.putAll(preshadeds);
	}
}
