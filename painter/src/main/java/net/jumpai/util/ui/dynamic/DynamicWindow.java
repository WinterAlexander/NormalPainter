package net.jumpai.util.ui.dynamic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import java.util.function.Supplier;

/**
 * DynamicUI table that updates its DynamicUI component childrens
 * <p>
 * Created on 2018-07-04.
 *
 * @author Alexander Winter
 */
public class DynamicWindow extends Window implements DynamicUI
{
	public DynamicWindow(String title, Skin skin)
	{
		super(title, skin);
	}

	public DynamicWindow(String title, Skin skin, String styleName)
	{
		super(title, skin, styleName);
	}

	public DynamicWindow(String title, WindowStyle style)
	{
		super(title, style);
	}

	public Cell<DynamicLabel> addDynamic(Supplier<CharSequence> text)
	{
		if(getSkin() == null)
			throw new IllegalStateException("Table must have a skin set to use this method.");

		return add(new DynamicLabel(text, getSkin()));
	}

	public Cell<DynamicLabel> addDynamic(Supplier<CharSequence> text, String labelStyleName)
	{
		if(getSkin() == null)
			throw new IllegalStateException("Table must have a skin set to use this method.");
		return add(new DynamicLabel(text, getSkin().get(labelStyleName, LabelStyle.class)));
	}

	public Cell<DynamicLabel> addDynamic(Supplier<CharSequence> text, String fontName, Color color)
	{
		if(getSkin() == null)
			throw new IllegalStateException("Table must have a skin set to use this method.");
		return add(new DynamicLabel(text, new LabelStyle(getSkin().getFont(fontName), color)));
	}

	public Cell<DynamicLabel> addDynamic(Supplier<CharSequence> text, String fontName, String colorName)
	{
		if(getSkin() == null)
			throw new IllegalStateException("Table must have a skin set to use this method.");
		return add(new DynamicLabel(text, new LabelStyle(getSkin().getFont(fontName), getSkin().getColor(colorName))));
	}

	@Override
	public void update()
	{
		for(Cell cell : getCells())
			if(cell.getActor() instanceof DynamicUI)
				((DynamicUI)cell.getActor()).update();

		invalidateHierarchy();
		pack();
	}
}
