package net.jumpai.util.ui.scrollpane;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.jumpai.util.ui.ProgressSpinner;

import static net.jumpai.util.async.AsyncCaller.async;
import static net.jumpai.util.async.GdxCallback.gdxWrapper;
import static net.jumpai.util.gdx.GdxUtil.postRunnable;

/**
 * A list of possibly infinite elements that is lazyly loaded using a
 * {@link LazyScrollPane}
 * <p>
 * Created on 2019-09-05.
 *
 * @author Alexander Winter
 */
public abstract class LazyListView<T> extends LazyScrollPane implements LazyScrollPaneListener
{
	protected short pageSize = 20;

	protected final Table container;
	protected final ProgressSpinner bottomSpinner, reloadSpinner;

	protected long endIndex;
	protected boolean loading, done = false, reload = false;

	public LazyListView(Drawable spinnerDrawable, Skin skin)
	{
		this(spinnerDrawable, skin, "default");
	}

	public LazyListView(Drawable spinnerDrawable, Skin skin, String styleName)
	{
		super(new Table(skin), skin, styleName);
		addListener(this);

		container = getActor();
		container.top();
		reloadSpinner = new ProgressSpinner(spinnerDrawable);
		bottomSpinner = new ProgressSpinner(spinnerDrawable);

		postRunnable(this::reload);
	}

	protected abstract void buildElements(T content);

	protected abstract boolean isDone(T content);

	protected abstract T fetch(long index, short count) throws Exception;

	protected abstract void handleError(Exception ex);

	protected void receive(T content)
	{
		if(reload)
		{
			container.clearChildren();
			setScrollY(0f);
			setVelocityY(0f);
		}
		else
			removeBottomSpinner();

		buildElements(content);

		if(isDone(content))
			done = true;
		else
			container.add(bottomSpinner).expandX().center().row();
	}

	protected void removeBottomSpinner()
	{
		Cell cell = container.getCells().get(container.getCells().size - 1);
		if(cell.getActor() instanceof ProgressSpinner)
			cell.setActor(null);
	}

	@Override
	public void loadMore()
	{
		if(loading || done || container.getChildren().size == 0)
			return;

		async(this::fetch, endIndex, pageSize)
		.then(this::receive, gdxWrapper)
		.except(Exception.class, this::handleError, gdxWrapper)
		.always(() -> loading = false, gdxWrapper)
		.execute();

		loading = true;
		endIndex += pageSize;
	}

	@Override
	public void reload()
	{
		if(loading)
			return;

		cancel();
		container.clearChildren();
		container.add(reloadSpinner).expandX().center().row();
		setScrollY(0f);
		setVelocityY(0f);
		super.act(0f);

		async(this::fetch, 0L, pageSize)
		.then(this::receive, gdxWrapper)
		.except(Exception.class, this::handleError, gdxWrapper)
		.always(() -> {
			loading = false;
			reload = false;
		}, gdxWrapper)
		.execute();

		loading = true;
		reload = true;
		done = false;
		endIndex = pageSize;
	}

	@Override
	public Table getActor()
	{
		return (Table)super.getActor();
	}
}
