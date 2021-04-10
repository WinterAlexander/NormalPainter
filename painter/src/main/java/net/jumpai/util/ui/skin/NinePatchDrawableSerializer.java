package net.jumpai.util.ui.skin;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.ReadOnlySerializer;
import com.badlogic.gdx.utils.JsonValue;
import net.jumpai.util.ui.drawable.ScaleFixNinePatchDrawable;

import static net.jumpai.util.gdx.Scene2dUtil.scale9Patch;

/**
 * Serializer to load nine patches from skin
 * <p>
 * Created on 2018-04-07.
 *
 * @author Alexander Winter
 */
public class NinePatchDrawableSerializer extends ReadOnlySerializer<NinePatchDrawable>
{
	private final Skin skin;

	public NinePatchDrawableSerializer(Skin skin)
	{
		this.skin = skin;
	}

	@Override
	public NinePatchDrawable read(Json json, JsonValue jsonData, Class type)
	{
		String regionName = json.readValue("patch", String.class, jsonData);

		if(regionName == null)
			throw new IllegalArgumentException("region of NinePatchDrawable must not be null");

		NinePatch ninePatch = new NinePatch(skin.getPatch(regionName));
		TextureRegion region = skin.getRegion(regionName);

		float width = json.readValue("minWidth", float.class, 1f, jsonData);
		float height = json.readValue("minHeight", float.class, 1f, jsonData);

		float scaleX = width / region.getRegionWidth();
		float scaleY = height / region.getRegionHeight();

		scale9Patch(ninePatch, scaleX, scaleY);

		return new ScaleFixNinePatchDrawable(ninePatch);
	}
}
