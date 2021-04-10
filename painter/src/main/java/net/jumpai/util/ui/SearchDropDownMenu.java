package net.jumpai.util.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import net.jumpai.render.AssetSupplier;
import net.jumpai.util.ui.textfield.TextFieldIcon;

import java.util.function.Consumer;

/**
 * Dropdown menu triggered by a textfield.
 * <p>
 * Created on 2018-03-30.
 *
 * @author Cedric Martens
 * @author Alexander Winter
 */
public class SearchDropDownMenu<T extends Actor> extends DropDownMenu<T>
{
	private final TextFieldIcon search;
	private Consumer<String> searchAction;

	public SearchDropDownMenu(AssetSupplier assets, Skin skin)
	{
		super(skin);

		search = new TextFieldIcon(
				skin.getDrawable("checkmark"),
				skin,
				"default");
		add(search).growX();
		displaySuccess(false);
		setTouchable(Touchable.enabled);
		content.setRound(false);
		content.align(Align.top);
	}

	@Override
	protected void updateContentTable()
	{
		content.toFront();
		content.setWidth(search.getWidth());
	}

	public void setSearchAction(Consumer<String> searchAction)
	{
		this.searchAction = searchAction;
		search.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				search();
			}
		});
	}

	public void displaySuccess(boolean success)
	{
		search.getIcon().setVisible(success);
	}

	public TextField getTextField()
	{
		return search.getTextField();
	}

	public void search()
	{
		displaySuccess(false);
		String query = getTextField().getText();
		searchAction.accept(query);
	}
}
