package net.jumpai.util.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Table displaying elements in a menu that drops down
 * <p>
 * Created on 2018-03-30.
 *
 * @author Cedric Martens
 */
public abstract class DropDownMenu<T extends Actor> extends Table
{
	protected final Table content;

	public DropDownMenu(Skin skin)
	{
		super(skin);
		content = new Table(skin);
		addActor(content);
	}

	@SafeVarargs
	public final void addActorsToDropDown(T... actors)
	{
		clear();
		for(Actor a : actors)
			content.add(a).growX().row();

		updateContentTable();
	}

	protected abstract void updateContentTable();

	public void clear()
	{
		content.clearChildren();
	}
}
