package net.jumpai.util.ui.dynamic;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;

import java.util.function.Supplier;

/**
 * {@link TextTooltip} that updates its content dynamically
 * <p>
 * Created on 2017-11-18.
 *
 * @author Alexander Winter
 */
public class DynamicTextTooltip extends TextTooltip implements DynamicUI
{
	private final Supplier<String> supplier;

	public DynamicTextTooltip(Supplier<String> text, Skin skin)
	{
		super(text.get(), skin);
		this.supplier = text;
	}

	public DynamicTextTooltip(Supplier<String> text, Skin skin, String styleName)
	{
		super(text.get(), skin, styleName);
		this.supplier = text;
	}

	public DynamicTextTooltip(Supplier<String> text, TextTooltipStyle style)
	{
		super(text.get(), style);
		this.supplier = text;
	}

	public DynamicTextTooltip(Supplier<String> text, TooltipManager manager, Skin skin)
	{
		super(text.get(), manager, skin);
		this.supplier = text;
	}

	public DynamicTextTooltip(Supplier<String> text, TooltipManager manager, Skin skin, String styleName)
	{
		super(text.get(), manager, skin, styleName);
		this.supplier = text;
	}

	public DynamicTextTooltip(Supplier<String> text, TooltipManager manager, TextTooltipStyle style)
	{
		super(text.get(), manager, style);
		this.supplier = text;
	}

	@Override
	public void update()
	{
		getActor().setText(supplier.get());
	}
}
