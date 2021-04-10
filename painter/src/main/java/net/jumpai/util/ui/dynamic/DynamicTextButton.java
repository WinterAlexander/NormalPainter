package net.jumpai.util.ui.dynamic;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.function.Supplier;

public class DynamicTextButton extends TextButton implements DynamicUI
{
    private Supplier<String> source;

    public DynamicTextButton(Supplier<String> source, Skin skin)
    {
        this(source, skin.get(TextButtonStyle.class));
    }

    public DynamicTextButton(Supplier<String> source, Skin skin, String styleName)
    {
        this(source, skin.get(styleName, TextButtonStyle.class));
    }

    public DynamicTextButton(Supplier<String> source, TextButtonStyle style)
    {
        super(source.get(), style);
        this.source = source;
    }

    @Override
    public void update() {
        setText(source.get());
    }
}
