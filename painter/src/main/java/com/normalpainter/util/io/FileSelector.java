package com.normalpainter.util.io;

import java.io.File;
import java.util.function.Consumer;

/**
 * Interface to ask computer to select a file, implementation is likely to open
 * a system-specific dialog to select the file
 * <p>
 * Created on 2018-05-16.
 *
 * @author Cedric Martens
 * @author Alexander Winter
 */
public interface FileSelector
{
	/**
	 * Selects a file and returns it using the specified callback. If no file
	 * was selected, the callback is never called
	 *
	 * @param callback callback to get the selected file
	 */
	void selectFile(Consumer<File> callback);

	/**
	 * Checks if the file selector implementation is supported on this system
	 * and returns true if it is
	 *
	 * @return true if the file selector is supported, otherwise false
	 */
	boolean isSupported();
}
