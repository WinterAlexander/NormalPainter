package com.normalpainter.app;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.normalpainter.util.ui.listener.ClickAdapter;
import com.normalpainter.util.ui.listener.SubmitListener;
import com.normalpainter.util.ui.AutoFocusTextFieldDialog;

import java.util.function.Consumer;

/**
 * Dialog to save a normal map in NormalPainter
 * <p>
 * Created on 2020-12-20.
 *
 * @author Alexander Winter
 */
public class SaveDialog extends AutoFocusTextFieldDialog
{
	public SaveDialog(Skin skin, String prevName, Consumer<String> saveAction)
	{
		super("", skin);

		getContentTable().pad(20);
		getButtonTable().padBottom(10);

		Label label = new Label("Please enter file name to save", skin);

		TextField fileName = getTextField();
		fileName.setText(prevName);

		fileName.addListener(new SubmitListener(() -> {
			saveAction.accept(fileName.getText());
			remove();
		}));
		fileName.addCaptureListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode)
			{
				if(keycode == Keys.ESCAPE)
				{
					remove();
					return true;
				}
				return false;
			}
		});

		TextButton save = new TextButton("Save", skin);
		save.addListener(new ClickAdapter(() -> {
			saveAction.accept(fileName.getText());
			remove();
		}));

		TextButton cancel = new TextButton("Cancel", skin);
		cancel.addListener(new ClickAdapter(this::remove));

		getContentTable().add(label).left().padLeft(10f).row();
		getContentTable().add(fileName).width(400f).row();

		getButtonTable().add(save).width(150f).pad(5f);
		getButtonTable().add(cancel).width(150f).pad(5f);
	}

	@Override
	public Dialog show(Stage stage)
	{
		super.show(stage, Actions.delay(0f));
		setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
		return this;
	}
}