package net.jumpai.client.ui.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import net.jumpai.util.ui.listener.ClickAdapter;

/**
 * Dialog with multiple buttons
 * <p>
 * Created on 2018-07-28.
 *
 * @author Cedric Martens
 * @author Alexander Winter
 */
public class MultiButtonDialog extends Dialog
{
	public MultiButtonDialog(String text, Skin skin, ButtonDefinition... buttons)
	{
		this(text, skin, 500f, buttons);
	}

	public MultiButtonDialog(String text, Skin skin, float width, ButtonDefinition... buttons)
	{
		super("", skin);
		getContentTable().pad(20);
		getButtonTable().padBottom(20f).padTop(20f);

		Label label = new Label(text, getSkin());
		label.setWrap(true);
		getContentTable().add(label).prefWidth(width);

		for(ButtonDefinition def : buttons)
		{
			TextButton button = new TextButton(def.name, getSkin(), def.styleName);
			button.padLeft(10f).padRight(10f);

			button.addListener(new ClickAdapter(() -> {
				def.action.accept(this);
				hide();
			}));
			getButtonTable().add(button).uniform().fill().padRight(10f).padLeft(10f).minWidth(100f);
		}
	}
}
