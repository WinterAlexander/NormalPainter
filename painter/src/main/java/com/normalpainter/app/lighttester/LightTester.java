package com.normalpainter.app.lighttester;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Launches the LightTesterApp
 * <p>
 * Created on 2020-05-17.
 *
 * @author Alexander Winter
 */
public class LightTester
{
	public static void main(String[] args) throws Throwable
	{
		String texturePath = "texture.png";
		String normalPath = "normal.png";

		if(args.length > 0)
			texturePath = args[0];

		if(args.length > 1)
			normalPath = args[1];

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30 = true;
		config.width = 1280;
		config.height = 720;

		new LwjglApplication(new LightTesterApp(texturePath, normalPath), config);
	}
}
