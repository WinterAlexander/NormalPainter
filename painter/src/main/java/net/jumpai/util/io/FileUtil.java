package net.jumpai.util.io;

import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Gives access to useful method while working with files and resources
 * <p>
 * Created on 2016-01-10.
 *
 * @author Alexander Winter
 */
public class FileUtil
{
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final Date date = new Date();

	private FileUtil() {}

	public static void appendToFile(File file, String line)
	{
		try
		{
			file.getParentFile().mkdirs();
			file.createNewFile();

			try(FileWriter writer = new FileWriter(file, true))
			{
				writer.write(line);
			}
		}
		catch(IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Ensures specified directory exists
	 *
	 * @param directory directory to be sure exists
	 * @throws IOException if an I/O error occurs
	 */
	public static void ensureDirectory(File directory) throws IOException
	{
		ensureNotNull(directory, "directory");

		if(directory.exists())
		{
			if(directory.isDirectory())
				return;

			if(!directory.delete())
				throw new IOException("Can't delete file " + directory.getAbsolutePath() + " to get equivalent directory");
		}

		if(!directory.mkdirs() && (!directory.isDirectory()))
			throw new IOException("Can't create directory " + directory.getAbsolutePath());
	}

	/**
	 * Ensures specified file exists
	 *
	 * @param file file to be sure exists
	 * @throws IOException if an I/O error occurs
	 */
	public static void ensureFile(File file) throws IOException
	{
		ensureNotNull(file, "file");

		if(file.getParentFile() != null)
			ensureDirectory(file.getParentFile());

		if(file.exists())
		{
			if(file.isFile())
				return;

			if(!file.delete())
				throw new IOException("Can't delete directory " + file.getAbsolutePath() + " to get equivalent file");
		}

		if(!file.createNewFile() && (!file.isFile()))
			throw new IOException("Can't create file " + file.getAbsolutePath());
	}

	/**
	 * Deletes specified file
	 * @param file file to delete
	 *
	 * @throws IOException if an I/O error occurs
	 */
	public static void deleteFile(File file) throws IOException
	{
		if(!file.delete())
			throw new IOException("Couldn't delete file " + file.getAbsolutePath());
	}

	/**
	 * Deletes the specified file and any children is has. An error may leave
	 * the directory structure partially deleted.
	 *
	 * @param file file to delete
	 * @throws IOException if an I/O error occurs
	 */
	public static void deleteRecursively(File file) throws IOException
	{
		if(file.isDirectory())
			for(File child : listFiles(file))
				deleteRecursively(child);

		deleteFile(file);
	}

	/**
	 * Gets the file of the "AppData" directory, the folder in which application data should be saved
	 *
	 * @return file representing AppData directory
	 */
	public static File getAppData()
	{
		return new File(getAppDataPath());
	}

	/**
	 * Gets the file of the "Home" directory
	 *
	 * @return file representing Home directory
	 */
	public static File getHomeFolder() { return new File(getHomeFolderPath()); }

	/**
	 * Gets the path of the "AppData" directory, the folder in which application data should be saved
	 *
	 * @return path ot AppData directory
	 */
	public static String getAppDataPath()
	{
		String osName = System.getProperty("os.name").toUpperCase(Locale.ENGLISH);

		if(osName.contains("WIN"))
			return System.getenv("APPDATA");

		if(osName.contains("MAC"))
			return getHomeFolderPath() + "/Library/Application Support";

		if(osName.contains("NUX"))
			return getHomeFolderPath() + "/.config";

		return System.getProperty("user.dir");
	}

	/**
	 * Gets the path of the "Home" directory
	 *
	 * @return path to Home directory
	 */
	public static String getHomeFolderPath()
	{
		return System.getProperty("user.home");
	}

	/**
	 * Resolves path that refers to the home directory with a ~ at the beginning
	 * of the path by replacing the character by the actual home directory. If
	 * the path do not refer to a home directory, the argument is returned
	 * without modification.
	 *
	 * @param path path to resolve
	 * @return resolved path
	 */
	public static String resolveHomeInPath(String path)
	{
		return path.replaceFirst("^~", Matcher.quoteReplacement(System.getProperty("user.home")));
	}

	/**
	 * Gets the attributes of the manifest of the jar of this class
	 *
	 * @return attributes of manifest
	 * @throws IOException if an I/O error occurs
	 */
	public static Attributes getManifestAttributes() throws IOException
	{
		return new Manifest(getManifestPath().openStream()).getMainAttributes();
	}

	/**
	 * Gets the path of the manifest of the jar of this class
	 *
	 * @return path of the manifest
	 * @throws IOException if an I/O error occur
	 */
	public static URL getManifestPath() throws IOException
	{
		Class clazz = FileUtil.class;
		String classPath = clazz.getResource(clazz.getSimpleName() + ".class").toString();

		if(!classPath.startsWith("jar"))
			throw new IOException("class does not come from jar");

		return new URL(classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF");
	}

	/**
	 * Gets the input stream for a specified resource inside the jar of this class
	 *
	 * @param resourceName name of the resource
	 * @return inputstream reading the resource
	 * @throws FileNotFoundException if the resource couldn't be found
	 */
	public static InputStream resourceAsStream(String resourceName) throws FileNotFoundException
	{
		if(!resourceName.startsWith("/"))
			resourceName = "/" + resourceName;

		InputStream stream = FileUtil.class.getResourceAsStream(resourceName);

		if(stream == null)
			throw new FileNotFoundException("Resource not found: " + resourceName);

		return stream;
	}

	/**
	 * Lists all files in specified directory
	 *
	 * @param directory directory to list files in
	 * @return array of children of this directory
	 * @throws IOException if an I/O error occurs
	 */
	public static File[] listFiles(File directory) throws IOException
	{
		if(!directory.isDirectory())
			throw new IOException("Specified file is not a directory");

		File[] files = directory.listFiles();

		if(files == null)
			throw new IOException("listFiles call returned null");

		return files;
	}

	/**
	 * Returns a file instance named after the current date into specified directory
	 *
	 * @param directory directory containing the log file
	 * @return log file of current date
	 */
	public static File getLogFile(File directory)
	{
		return new File(directory, getDailyLogfileName());
	}

	/**
	 * Returns a filename composed of the current date and the ".log" extension.
	 * @return a filename
	 */
	public static String getDailyLogfileName()
	{
		date.setTime(System.currentTimeMillis());
		return dateFormat.format(date) + ".log";
	}

	/**
	 * Returns the specified filename without its extension
	 *
	 * @param filename filename to remove extension from
	 * @return filename without extension
	 */
	public static String withoutExtension(String filename)
	{
		ensureNotNull(filename, "filename");

		return filename.replaceFirst("[.][^.]+$", "");
	}

	/**
	 * Converts the specified {@link File} to a libGDX's {@link FileHandle}
	 *
	 * @param file file to convert
	 * @return converted file
	 */
	public static FileHandle toGdx(File file)
	{
		return new FileHandle(file);
	}
}