package net.jumpai.tools.lighttester;

import net.jumpai.render.ScreenStackGame;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Application to test dynamic lighting with a provided texture and normal
 * <p>
 * Created on 2020-05-17.
 *
 * @author Alexander Winter
 */
public class LightTesterApp extends ScreenStackGame
{
	private final String texturePath, normalPath;

	public LightTesterApp(String texturePath, String normalPath)
	{
		ensureNotNull(texturePath, "texturePath");
		ensureNotNull(normalPath, "normalPath");
		this.texturePath = texturePath;
		this.normalPath = normalPath;
	}

	@Override
	public void create()
	{
		LightTesterScreen screen = new LightTesterScreen(texturePath, normalPath);
		screen.create();
		setScreen(screen);
	}
}
