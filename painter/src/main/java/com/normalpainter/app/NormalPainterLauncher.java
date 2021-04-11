package com.normalpainter.app;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Launcher for the NormalPainter app
 * <p>
 * Created on 2020-12-19.
 *
 * @author Alexander Winter
 */
public class NormalPainterLauncher
{
	public static void main(String[] args) throws Throwable
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30 = true;
		config.width = 1280;
		config.height = 720;

		new LwjglApplication(new NormalPainterApp(), config) {
			@Override
			public void exit() {
				((NormalPainterApp)getApplicationListener()).safeQuit(super::exit);
			}
		};
	}
}
