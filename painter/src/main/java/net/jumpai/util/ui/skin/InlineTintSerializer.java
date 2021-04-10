package net.jumpai.util.ui.skin;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.ReadOnlySerializer;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;
import net.jumpai.util.ui.drawable.TintedDrawable;

import static net.jumpai.util.ReflectionUtil.get;
import static net.jumpai.util.ReflectionUtil.getType;
import static net.jumpai.util.ReflectionUtil.has;
import static net.jumpai.util.ReflectionUtil.set;

/**
 * Serializer that is able to lo
 * <p>
 * Created on 2019-06-19.
 *
 * @author Alexander Winter
 */
public class InlineTintSerializer<T> extends ReadOnlySerializer<T>
{
	@SuppressWarnings("unchecked")
	@Override
	public T read(Json json, JsonValue jsonData, Class type)
	{
		try
		{
			Object style = type.newInstance();

			boolean ignoreUnknownFields = get(json, "ignoreUnknownFields");

			json.setIgnoreUnknownFields(true);
			json.readFields(style, jsonData);
			json.setIgnoreUnknownFields(ignoreUnknownFields);

			for(JsonValue child = jsonData.child; child != null; child = child.next)
			{
				if(!child.name.endsWith("Tint") || !child.name.startsWith("!"))
				{
					if(ignoreUnknownFields && !has(type, child.name))
					{
						SerializationException ex = new SerializationException(
								"Field not found: " + child.name + " (" + type.getName() + ")");
						ex.addTrace(child.trace());
						throw ex;
					}
					continue;
				}

				String drawableName = child.name.substring(1, child.name.length() - 4);

				if(!has(type, drawableName))
				{
					if(ignoreUnknownFields)
						continue;

					SerializationException ex = new SerializationException(
							"Field not found for tinting: " + drawableName + " (" + type.getName() + ")");
					ex.addTrace(child.trace());
					throw ex;
				}

				if(getType(type, drawableName) != Drawable.class)
				{
					if(ignoreUnknownFields)
						continue;

					SerializationException ex = new SerializationException(
							"Cannot tint: " + drawableName + ", not a drawable (" + type.getName() + ")");
					ex.addTrace(child.trace());
					throw ex;
				}

				TintReference tintReference = json.readValue(TintReference.class, child);
				set(style, drawableName, new TintedDrawable(get(style, tintReference.base), tintReference.color));

			}
			return (T)style;
		}
		catch(IllegalAccessException | InstantiationException ex)
		{
			throw new GdxRuntimeException(ex);
		}
	}

	private static class TintReference
	{
		public String base;
		public Color color;
	}
}
