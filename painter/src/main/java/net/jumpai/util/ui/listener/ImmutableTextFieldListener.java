package net.jumpai.util.ui.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * {@link InputListener} that makes a {@link TextField} immutable.
 * <p>
 * Created on 2019-03-20.
 *
 * @author Alexander Winter
 */
public class ImmutableTextFieldListener extends InputListener
{
	private final TextField textField;
	private final boolean autoCopy;

	public ImmutableTextFieldListener(TextField textField, boolean autoCopy)
	{
		ensureNotNull(textField, "textField");

		this.textField = textField;
		this.autoCopy = autoCopy;
	}

	@Override
	public boolean keyDown(InputEvent event, int keycode)
	{
		//to prevent pasting with Ctrl + V (which does not trigger keyTyped)

		if(autoCopy)
			Gdx.app.getClipboard().setContents(textField.getText());
		textField.getStage().setKeyboardFocus(null);
		return false;
	}

	@Override
	public boolean keyUp(InputEvent event, int keycode)
	{
		//to prevent pasting with Ctrl + V (which does not trigger keyTyped)

		textField.getStage().setKeyboardFocus(null);
		return false;
	}
}
