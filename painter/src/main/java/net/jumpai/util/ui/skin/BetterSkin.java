package net.jumpai.util.ui.skin;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.ReadOnlySerializer;
import com.badlogic.gdx.utils.JsonValue;
import net.jumpai.util.ui.drawable.MultiDrawable;
import net.jumpai.util.ui.textfield.validated.ValidatedTextFieldStyle;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Skin with serialization improvements to support custom fonts and other
 * features
 * <p>
 * Created on 2019-06-19.
 *
 * @author Alexander Winter
 */
public class BetterSkin extends Skin
{
	private final ShaderProgram tintShader;

	public BetterSkin(TextureAtlas atlas, ShaderProgram tintShader)
	{
		super(atlas);
		ensureNotNull(tintShader, "tintShader");
		this.tintShader = tintShader;
	}

	@Override
	protected Json getJsonLoader(FileHandle skinFile)
	{
		Json json = super.getJsonLoader(skinFile);

		json.setSerializer(DynamicFont.class, new ReadOnlySerializer<DynamicFont>() {
			public DynamicFont read(Json json, JsonValue jsonData, Class type) {
				DynamicFont font = new DynamicFont();
				font.family = skinFile.parent().child(json.readValue("family", String.class, jsonData)).path();
				font.size = json.readValue("size", int.class, -1, jsonData);

				if(font.size < 0)
					throw new IllegalArgumentException("DynamicFont size is required and must be positive");

				add(jsonData.name(), new BitmapFont(), BitmapFont.class);
				return font;
			}
		});


		json.setSerializer(NinePatchDrawable.class, new NinePatchDrawableSerializer(this));
		json.setSerializer(MultiDrawable.class, new MultiDrawableSerializer(this));
		json.setSerializer(net.jumpai.util.ui.drawable.TintedDrawable.class, new TintedDrawableSerializer(tintShader));
		json.setSerializer(ButtonStyle.class, new InlineTintSerializer<>());
		json.setSerializer(TextButtonStyle.class, new InlineTintSerializer<>());
		json.setSerializer(ImageButtonStyle.class, new InlineTintSerializer<>());
		json.setSerializer(CheckBoxStyle.class, new InlineTintSerializer<>());
		json.setSerializer(WindowStyle.class, new InlineTintSerializer<>());
		json.setSerializer(LabelStyle.class, new InlineTintSerializer<>());
		json.setSerializer(TextFieldStyle.class, new InlineTintSerializer<>());
		json.setSerializer(ValidatedTextFieldStyle.class, new InlineTintSerializer<>());

		return json;
	}
}
