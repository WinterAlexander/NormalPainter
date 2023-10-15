package com.normalpainter.app.dialog;

import com.badlogic.gdx.utils.Array;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.io.File;
import java.util.function.Consumer;

import static com.winteralexander.gdx.utils.async.AsyncCaller.async;

/**
 * Swing implementation of FileSelector
 * <p>
 * Created on 2018-05-16.
 *
 * @author Alexander Winter
 */
public class SwingDesktopFileSelector
{
	private final Array<JFileChooser> currentChoosers = new Array<>();

	private final Object arrLock = new Object();

	private File prevDir = null;

	public void selectFile(Consumer<File> callback, int dialogType)
	{
		synchronized(arrLock)
		{
			for(JFileChooser chooser : currentChoosers)
				chooser.cancelSelection();
		}

		async(() -> {
			JFileChooser chooser = new JFileChooser();
			JFrame f = new JFrame();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setDialogType(dialogType);

			if(prevDir != null)
				chooser.setCurrentDirectory(prevDir);
			else
				chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

			f.setVisible(true);
			f.toFront();
			f.setVisible(false);

			synchronized(arrLock)
			{
				currentChoosers.add(chooser);
			}

			if(chooser.showDialog(f, null) == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null)
			{
				prevDir = chooser.getSelectedFile().getParentFile();
				callback.accept(chooser.getSelectedFile());
			}

			synchronized(arrLock)
			{
				currentChoosers.removeValue(chooser, true);
			}
			f.dispose();
		}).execute();
	}
}
