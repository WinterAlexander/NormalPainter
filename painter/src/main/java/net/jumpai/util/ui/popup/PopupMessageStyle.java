package net.jumpai.util.ui.popup;

import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;

/**
 * Style for a {@link PopupMessage}
 * <p>
 * Created on 2019-03-30.
 *
 * @author Alexander Winter
 */
public class PopupMessageStyle extends LabelStyle
{
	public int align = Align.bottomRight;

	public float width = 400f;
	public float margin;

	public PopupMessageStyle() {}

	public PopupMessageStyle(PopupMessageStyle style)
	{
		super(style);

		this.align = style.align;
		this.width = style.width;
		this.margin = style.margin;
	}
}
