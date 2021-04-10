package net.jumpai.util.ui;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import static net.jumpai.util.gdx.Scene2dUtil.isKeyEvent;

/**
 * {@link TextField} that doesn't grab it's keyboard events
 * <p>
 * Created on 2018-05-02.
 *
 * @author Alexander Winter
 */
public class NonGrabbingTextField extends TextField
{
	protected boolean grabEvents = false;

	public NonGrabbingTextField(String text, Skin skin)
	{
		super(text, skin);
	}

	public NonGrabbingTextField(String text, Skin skin, String styleName)
	{
		super(text, skin, styleName);
	}

	public NonGrabbingTextField(String text, TextFieldStyle style)
	{
		super(text, style);
	}

	@Override
	protected InputListener createInputListener()
	{
		return new NonGrabbingTextFieldListener();
	}

	public class NonGrabbingTextFieldListener extends TextFieldClickListener
	{
		public NonGrabbingTextFieldListener() {}

		@Override
		public boolean handle(Event e)
		{
			return super.handle(e) && (grabEvents || !(e instanceof InputEvent) || !isKeyEvent((InputEvent)e));
		}
	}
}
