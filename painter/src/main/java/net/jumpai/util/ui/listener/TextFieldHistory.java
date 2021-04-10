package net.jumpai.util.ui.listener;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Listener that allows a TextField to have a browsable history (with up and
 * down keys)
 * <p>
 * Created on 2019-01-14.
 *
 * @author Alexander Winter
 */
public class TextFieldHistory extends InputListener
{
	private final Array<String> history = new Array<>();
	private int currentIndex = 0;
	private String buffer = "";

	private final TextField textField;

	public TextFieldHistory(TextField textField)
	{
		ensureNotNull(textField, "textField");
		this.textField = textField;
	}

	public void add(String message)
	{
		history.add(message);
		currentIndex = 0;
		buffer = "";
	}

	@Override
	public boolean keyDown(InputEvent event, int keycode)
	{
		if(keycode == Keys.UP)
		{
			if(currentIndex >= history.size)
				return true;

			if(currentIndex == 0)
				buffer = textField.getText();
			currentIndex++;
			textField.setText(history.get(history.size - currentIndex));
			textField.setCursorPosition(textField.getText().length());
			return true;
		}
		else if(keycode == Keys.DOWN)
		{
			if(currentIndex <= 0)
				return true;

			currentIndex--;
			if(currentIndex == 0)
				textField.setText(buffer);
			else
				textField.setText(history.get(history.size - currentIndex));
			textField.setCursorPosition(textField.getText().length());
			return true;
		}
		return false;
	}
}
