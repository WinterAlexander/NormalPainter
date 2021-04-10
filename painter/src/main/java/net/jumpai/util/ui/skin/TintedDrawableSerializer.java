package net.jumpai.util.ui.skin;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.ReadOnlySerializer;
import com.badlogic.gdx.utils.JsonValue;
import net.jumpai.util.ui.drawable.ShaderDrawable;
import net.jumpai.util.ui.drawable.TintedDrawable;

import static net.jumpai.util.ReflectionUtil.get;
import static net.jumpai.util.Validation.ensureNotNull;

/**
 * {@link TintedDrawable} serializer which supports fancy tinted drawables
 * with a tint shader
 * <p>
 * Created on 2020-03-15.
 *
 * @author Alexander Winter
 */
public class TintedDrawableSerializer extends ReadOnlySerializer<TintedDrawable>
{
	private final ShaderProgram tintShader;

	public TintedDrawableSerializer(ShaderProgram tintShader)
	{
		ensureNotNull(tintShader, "tintShader");
		this.tintShader = tintShader;
	}

	@Override
	public TintedDrawable read(Json json, JsonValue jsonData, Class type)
	{
		TintedDrawable style = new TintedDrawable();

		boolean ignoreUnknownFields = get(json, "ignoreUnknownFields");

		json.setIgnoreUnknownFields(true);
		json.readFields(style, jsonData);
		json.setIgnoreUnknownFields(ignoreUnknownFields);

		if(json.readValue("shader-tint", Boolean.class, false, jsonData))
			style.setDrawable(new ShaderDrawable(style.getDrawable(), tintShader));

		return style;
	}
}
