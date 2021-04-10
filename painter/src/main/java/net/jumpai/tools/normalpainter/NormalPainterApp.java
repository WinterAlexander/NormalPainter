package net.jumpai.tools.normalpainter;

import com.badlogic.gdx.utils.Array;
import net.jumpai.client.ui.dialog.ButtonDefinition;
import net.jumpai.client.ui.dialog.MultiButtonDialog;
import net.jumpai.render.ScreenStackGame;

/**
 * An application
 * <p>
 * Created on 2020-12-20.
 *
 * @author Alexander Winter
 */
public class NormalPainterApp extends ScreenStackGame<StageStackedScreen>
{
	public boolean unsavedChanges = false;

	NormalPainterScreen screen;

	@Override
	public void create()
	{
		screen = new NormalPainterScreen(this);
		screen.create();
		setScreen(screen);
	}

	public void safeQuit(Runnable runnable)
	{
		if(getScreen() != screen)
		{
			finishScreen();
			screen.lightScreen.normal.getTexture().dispose();
			screen.lightScreen.normal = null;
		}

		Runnable action = () -> {
			screen.saveConfig();
			runnable.run();
		};

		if(!unsavedChanges)
		{
			action.run();
			return;
		}

		Array<ButtonDefinition> buttons = new Array<>(ButtonDefinition.class);

		buttons.add(new ButtonDefinition(
				"Discard",
				d -> {
					action.run();
				}, "delta-small"));

		buttons.add(new ButtonDefinition(
				"Cancel",
				d -> {},
				"beta-small"));

		new MultiButtonDialog("You have unsaved changes", screen.assets.getSkin(), buttons.toArray())
				.show(getScreen().getStage());
	}
}
