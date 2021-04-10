package net.jumpai.util.ui.dynamic;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.util.function.Supplier;

/**
 * {@link TextField} that updates its content dynamically
 * <p>
 * Created on 2017-11-07.
 *
 * @author Alexander Winter
 */
public class DynamicTextField extends TextField implements DynamicUI
{
	private Supplier<String> source;

	public DynamicTextField(Supplier<String> source, Skin skin)
	{
		this(source, skin.get(TextFieldStyle.class));
	}

	public DynamicTextField(Supplier<String> source, Skin skin, String styleName) {
		this(source, skin.get(styleName, TextFieldStyle.class));
	}

	public DynamicTextField(Supplier<String> source, TextFieldStyle style)
	{
		super(source.get(), style);
		this.source = source;
	}

	@Override
	public void update()
	{
		setText(source.get());
	}

	public Supplier<String> getSource()
	{
		return source;
	}
}
