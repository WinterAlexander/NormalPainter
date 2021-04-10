package net.jumpai.client.ui.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import net.jumpai.render.AssetSupplier;
import net.jumpai.util.ui.listener.ClickAdapter;

import java.util.function.Consumer;

import static net.jumpai.util.gdx.Scene2dUtil.ignoreInputEvent;
import static net.jumpai.util.gdx.Scene2dUtil.performInputEvent;

/**
 * Dialog that displays an OK button after one or multiple lines.
 * <p>
 * Created on 2018-07-28.
 *
 * @author Cedric Martens
 * @author Alexander Winter
 */
public class OkDialog extends Dialog
{
	private final Runnable then;
	private final TextButton okButton;

	public OkDialog(AssetSupplier assets, String... text)
	{
		this(assets, d -> {}, text);
	}

	public OkDialog(AssetSupplier assets, Consumer<Dialog> then, String... text)
	{
		super("", assets.getSkin());
		this.then = () -> then.accept(this);
		getContentTable().pad(20);
		getButtonTable().padBottom(10);

		for(String s : text)
		{
			Label label = new Label(s, getSkin());
			label.setWrap(true);
			getContentTable().add(label).prefWidth(500);
			getContentTable().row();
		}

		okButton = new TextButton("Ok", getSkin());
		okButton.padLeft(30f).padRight(30f);

		okButton.addListener(new ClickAdapter(this::ok));

		getButtonTable().add(okButton).padRight(20f).padLeft(20f);
		addListener(event -> {
			if(isVisible() && event instanceof InputEvent)
				ignoreInputEvent((InputEvent)event);
			return false;
		});
	}

	private void pressOK()
	{
		performInputEvent(okButton, Type.touchDown);
	}

	private void releaseOK()
	{
		performInputEvent(okButton, Type.touchUp);
	}

	protected void ok()
	{
		then.run();
		hide();
	}
}
