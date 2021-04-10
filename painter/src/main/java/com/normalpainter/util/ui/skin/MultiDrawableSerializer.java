package com.normalpainter.util.ui.skin;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.ReadOnlySerializer;
import com.badlogic.gdx.utils.JsonValue;
import com.normalpainter.util.ui.drawable.MultiDrawable;

/**
 * Serializer to serialize multi drawables from skin
 * <p>
 * Created on 2018-04-08.
 *
 * @author Alexander Winter
 */
public class MultiDrawableSerializer extends ReadOnlySerializer<MultiDrawable>
{
	private final Skin skin;

	public MultiDrawableSerializer(Skin skin)
	{
		this.skin = skin;
	}

	@SuppressWarnings({"unchecked", "LibGDXUnsafeIterator"})
	@Override
	public MultiDrawable read(Json json, JsonValue jsonData, Class type)
	{
		Array<Drawable> drawables = new Array<>(Drawable.class);
		for(String name : (Array<String>)json.readValue("drawables", Array.class, String.class, jsonData))
			drawables.add(skin.getDrawable(name));

		return new MultiDrawable(drawables.toArray());
	}
}
