package net.jumpai.util.ui.linkbutton;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * {@link TextButton} that appears like a text link with a bar under its text
 * <p>
 * Created on 2019-06-06.
 *
 * @author Alexander Winter
 */
public class LinkButton extends TextButton
{
	private final LinkButtonStyle style;

	private final Color tmpColor = new Color();

	public LinkButton(String text, Skin skin)
	{
		this(text, skin, "default");
	}

	public LinkButton(String text, Skin skin, String styleName)
	{
		this(text, ensureNotNull(skin, "skin").get(styleName, LinkButtonStyle.class));
	}

	public LinkButton(String text, LinkButtonStyle style)
	{
		super(text, ensureNotNull(style, "style"));

		this.style = style;
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		tmpColor.set(getColor());
		tmpColor.a *= parentAlpha;
		batch.setColor(tmpColor);

		style.line.draw(batch, getX(), getY(), getX() + getWidth(), style.lineWidth);
	}
}
