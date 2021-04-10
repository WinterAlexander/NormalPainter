package com.normalpainter.tools.normalpainter;

import com.badlogic.gdx.utils.Array;
import com.normalpainter.FileSelector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.io.File;
import java.util.function.Consumer;

import static com.normalpainter.util.async.AsyncCaller.async;

/**
 * Swing implementation of FileSelector
 * <p>
 * Created on 2018-05-16.
 *
 * @author Alexander Winter
 */
public class DesktopFileSelector implements FileSelector
{
	private final Array<JFileChooser> currentChoosers = new Array<>();

	private final Object arrLock = new Object();

	private File prevDir = null;

	@Override
	public void selectFile(Consumer<File> callback)
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

			if(chooser.showSaveDialog(f) == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null)
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

	@Override
	public boolean isSupported()
	{
		return true;
	}
}
