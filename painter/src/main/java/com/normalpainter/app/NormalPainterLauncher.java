package com.normalpainter.app;

import com.badlogic.gdx.Files;
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
		config.addIcon("gfx/icon/icon_128.png", Files.FileType.Classpath);
		config.addIcon("gfx/icon/icon_64.png", Files.FileType.Classpath);
		config.addIcon("gfx/icon/icon_32.png", Files.FileType.Classpath);
		config.addIcon("gfx/icon/icon_16.png", Files.FileType.Classpath);

		new LwjglApplication(new NormalPainterApp(), config) {
			@Override
			public void exit() {
				((NormalPainterApp)getApplicationListener()).safeQuit(super::exit);
			}
		};
	}
}
