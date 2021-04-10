package com.normalpainter.render;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.StringBuilder;

import java.util.Scanner;

/**
 * {@link ShaderProgramLoader} that processes includes directives in shaders
 * <p>
 * Created on 2020-05-20.
 *
 * @author Alexander Winter
 */
public class IncludeShaderLoader extends ShaderProgramLoader
{
	public IncludeShaderLoader(FileHandleResolver resolver)
	{
		super(resolver);
	}

	@Override
	public ShaderProgram loadSync(AssetManager manager, String fileName, FileHandle file, ShaderProgramParameter parameter)
	{
		String vertFileName = null, fragFileName = null;

		if(parameter != null)
		{
			if(parameter.vertexFile != null)
				vertFileName = parameter.vertexFile;
			if(parameter.fragmentFile != null)
				fragFileName = parameter.fragmentFile;
		}

		FileHandle vertexFile = vertFileName == null ? file : resolve(vertFileName);
		FileHandle fragmentFile = fragFileName == null ? file : resolve(fragFileName);
		String vertexCode = vertexFile.readString();
		String fragmentCode = vertexFile.equals(fragmentFile) ? vertexCode : fragmentFile.readString();

		if(parameter != null)
		{
			if(parameter.prependVertexCode != null)
				vertexCode = parameter.prependVertexCode + vertexCode;

			if(parameter.prependFragmentCode != null)
				fragmentCode = parameter.prependFragmentCode + fragmentCode;
		}

		ObjectSet<FileHandle> alreadyProcessed = new ObjectSet<>();
		vertexCode = processIncludes(vertexFile.parent(), vertexCode, alreadyProcessed);
		alreadyProcessed.clear();
		fragmentCode = processIncludes(fragmentFile.parent(), fragmentCode, alreadyProcessed);

		ShaderProgram shaderProgram = new ShaderProgram(vertexCode, fragmentCode);

		if((parameter == null || parameter.logOnCompileFailure) && !shaderProgram.isCompiled())
			manager.getLogger().error("ShaderProgram " + fileName + " failed to compile:\n" + shaderProgram.getLog());
		else
			manager.getLogger().info("ShaderProgram " + fileName + " compiled successfully:\n" + shaderProgram.getLog());

		return shaderProgram;
	}

	private String processIncludes(FileHandle directory, String code, ObjectSet<FileHandle> alreadyProcessed)
	{
		Scanner scanner = new Scanner(code);
		StringBuilder sb = new StringBuilder();

		while(scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			// process the line
			if(line.startsWith("#include"))
			{
				String includePath = line.substring("#include".length()).trim();

				FileHandle includeFile = directory.child(includePath);

				if(alreadyProcessed.contains(includeFile))
					continue;

				alreadyProcessed.add(includeFile);

				String includeCode = includeFile.readString();
				includeCode = processIncludes(includeFile.parent(), includeCode, alreadyProcessed);
				sb.appendLine(includeCode);
			}
			else
				sb.appendLine(line);
		}

		scanner.close();
		return sb.toString();
	}
}
