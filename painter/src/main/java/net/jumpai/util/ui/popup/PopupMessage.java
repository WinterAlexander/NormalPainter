package net.jumpai.util.ui.popup;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.jumpai.util.math.OriginUtil;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Message shown for a specified duration in a {@link Stage}
 * <p>
 * Created on 2019-03-30.
 *
 * @author Alexander Winter
 */
public class PopupMessage extends Container<Label>
{
	private final PopupMessageStyle style;
	private final float duration;

	/**
	 * Creates a PopupMessage with the specified message and duration
	 *
	 * @param message message to display in the popup
	 * @param duration duration of the PopupMessage, in seconds
	 * @param skin skin to fetch the style from
	 * @param styleName name of the style to fetch in the skin
	 */
	public PopupMessage(String message, float duration, Skin skin, String styleName)
	{
		this(message, duration, skin.get(styleName, PopupMessageStyle.class));
	}

	/**
	 * Creates a PopupMessage with the specified message and duration
	 *
	 * @param message message to display in the popup
	 * @param duration duration of the PopupMessage, in seconds
	 * @param style style of this PopupMessage
	 */
	public PopupMessage(String message, float duration, PopupMessageStyle style)
	{
		super(new Label(message, style));

		this.style = style;
		this.duration = duration;

		getActor().setWrap(true);
		width(400f);
		layout();
		setWidth(400f);
		setHeight(getPrefHeight());
	}

	@Override
	protected void setStage(Stage stage)
	{
		if(stage != null)
		{
			Vector2 vec = OriginUtil.getOrigin(
					stage.getWidth() - 2 * style.margin,
					stage.getHeight() - 2 * style.margin,
					style.align);

			setPosition(-vec.x + style.margin, -vec.y + style.margin, style.align);
			setColor(1f, 1f, 1f, 0f);

			clearActions();
			addAction(sequence(
					fadeIn(0.15f),
					delay(duration),
					fadeOut(0.15f),
					Actions.removeActor()));
		}

		super.setStage(stage);
	}
}
