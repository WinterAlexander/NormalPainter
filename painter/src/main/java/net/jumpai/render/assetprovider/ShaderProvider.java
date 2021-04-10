package net.jumpai.render.assetprovider;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.jumpai.render.AssetSupplier;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * {@link AssetProvider} for a ShaderProgram
 * <p>
 * Created on 2020-09-11.
 *
 * @author Alexander Winter
 */
public class ShaderProvider implements AssetProvider<ShaderProgram>
{
	private final String shaderName, vertexShader, fragmentShader;
	private final String[] flags;

	public ShaderProvider(String shaderName,
	                      String vertexShader,
	                      String fragmentShader,
	                      String... flags)
	{
		ensureNotNull(shaderName, "shaderName");
		ensureNotNull(vertexShader, "vertexShader");
		ensureNotNull(fragmentShader, "fragmentShader");
		ensureNotNull(flags, "flags");

		this.shaderName = shaderName;
		this.vertexShader = vertexShader;
		this.fragmentShader = fragmentShader;
		this.flags = flags;
	}

	@Override
	public ShaderProgram resolve(AssetSupplier assetSupplier)
	{
		return assetSupplier.getShader(shaderName);
	}

	@Override
	public void load(AssetSupplier assetSupplier)
	{
		assetSupplier.loadShader(shaderName, vertexShader, fragmentShader, flags);
	}

	@Override
	public void finishLoading(AssetSupplier assetSupplier)
	{
		assetSupplier.finishLoadingAsset(shaderName);
	}
}
