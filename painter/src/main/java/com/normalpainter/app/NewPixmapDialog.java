package com.normalpainter.app;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.normalpainter.util.ui.listener.ClickAdapter;
import com.normalpainter.util.ui.listener.SubmitListener;
import com.normalpainter.render.AssetSupplier;
import com.normalpainter.util.math.NumberUtil;
import com.normalpainter.util.ui.AutoFocusTextFieldDialog;

import java.util.function.Consumer;

/**
 * Dialog to save a normal map in NormalPainter
 * <p>
 * Created on 2020-12-20.
 *
 * @author Alexander Winter
 */
public class NewPixmapDialog extends AutoFocusTextFieldDialog
{
	public NewPixmapDialog(AssetSupplier assets, Vector2 refSize, Vector2 flatSize, Consumer<Vector2> action)
	{
		super("", assets.getSkin());

		getContentTable().pad(20);
		getButtonTable().padBottom(10);

		Label label = new Label("Please enter the size for this new map", assets.getSkin());

		TextField width = getTextField();
		width.setText("" + Math.round(flatSize != null ? flatSize.x : refSize != null ? refSize.x : 100));

		TextField height = new TextField("" + Math.round(flatSize != null ? flatSize.y : refSize != null ? refSize.y : 100), assets.getSkin());

		Runnable submit = () -> {
			Vector2 vec = new Vector2(NumberUtil.tryParseFloat(width.getText(), -1f),
					NumberUtil.tryParseFloat(height.getText(), -1f));

			if(vec.x < 0f || vec.y < 0f || vec.x > 100000f || vec.y > 100000f)
			{
				new OkayDialog(assets, "Invalid size").show(getStage());
				return;
			}
			action.accept(vec);
			remove();
		};

		width.addListener(new SubmitListener(submit));
		width.addCaptureListener(new InputListener() {
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

		TextButton save = new TextButton("Ok", assets.getSkin());
		save.addListener(new ClickAdapter(submit));

		TextButton cancel = new TextButton("Cancel", assets.getSkin());
		cancel.addListener(new ClickAdapter(this::remove));

		getContentTable().add(label).colspan(3).left().padLeft(10f).row();
		getContentTable().add(width).width(200f);
		getContentTable().add("x");
		getContentTable().add(height).width(200f);

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