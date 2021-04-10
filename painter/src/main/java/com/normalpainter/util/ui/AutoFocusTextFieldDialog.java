package com.normalpainter.util.ui;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * Dialog that manages an auto-focus textfield.
 * The textfield must be added manually into the dialog.
 * <p>
 * Created on 2018-04-21.
 *
 * @author Cedric Martens
 * @author Alexander Winter
 */
public class AutoFocusTextFieldDialog extends SafeFadeOutDialog
{
	private final TextField textField;

	public AutoFocusTextFieldDialog(String title, Skin skin)
	{
		super(title, skin);
		textField = new TextField("", skin);
	}

	@Override
	public Dialog show(Stage stage, Action action)
	{
		super.show(stage, action);
		stage.setKeyboardFocus(textField);
		return this;
	}

	protected TextField getTextField()
	{
		return textField;
	}
}
