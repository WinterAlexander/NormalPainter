package net.jumpai.util.ui.actorhandle;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Style of an ActorHandle
 * <p>
 * Created on 2018-04-22.
 *
 * @author Alexander Winter
 */
public class ActorHandleStyle
{
	public Drawable background;

	public ActorHandleStyle() {}

	public ActorHandleStyle(ActorHandleStyle style)
	{
		this.background = style.background;
	}
}
