package com.normalpainter.app.dialog;

import java.awt.*;
import java.io.File;
import java.util.function.Consumer;

/**
 * AWT implementation of FileSelector
 * <p>
 * Created on 2023-10-11.
 *
 * @author Alexander Winter
 */
public class AWTDesktopFileSelector
{
	private File prevDir = null;

	public void selectFile(Consumer<File> callback, int dialogType)
	{
		FileDialog dialog = new FileDialog((Frame)null);
		if(prevDir != null)
			dialog.setDirectory(prevDir.getAbsolutePath());
		dialog.setMode(dialogType);
		dialog.setMultipleMode(false);
		dialog.setVisible(true);

		if(dialog.getFiles().length != 1)
			return;

		File file = dialog.getFiles()[0];

		prevDir = file.getParentFile().getAbsoluteFile();
		callback.accept(file);
	}
}
