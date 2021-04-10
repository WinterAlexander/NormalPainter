package net.jumpai.util.ui.textfield;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import net.jumpai.util.ui.listener.ClickAdapter;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * TextField that displays an image to the right of the text
 * <p>
 * Created on 2018-04-12.
 *
 * @author Alexander Winter
 * @author Cedric Martens
 */
public class TextFieldIcon extends Table
{
	private final TextField textField;
	private final Image icon;

	public TextFieldIcon(Drawable iconContent, Skin skin, String styleName)
	{
		this(iconContent, skin.get(styleName, TextFieldStyle.class));
	}

	public TextFieldIcon(Drawable iconContent, TextFieldStyle style)
	{
		ensureNotNull(iconContent, "iconContent");
		ensureNotNull(style, "style");

		this.icon = new Image(iconContent, Scaling.fit);
		this.textField = new TextField("", new TextFieldStyle(style));

		setBackground(textField.getStyle().background);

		textField.getStyle().background = null;
		textField.getStyle().focusedBackground = null;
		textField.getStyle().disabledBackground = null;

		textField.invalidate();

		add(textField).growX();
		add(icon);

		setTouchable(Touchable.enabled);
		addListener(new ClickAdapter(() -> getStage().setKeyboardFocus(textField)));
	}

	public TextField getTextField()
	{
		return textField;
	}

	public Image getIcon()
	{
		return icon;
	}
}
