package net.jumpai.render.spriter;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;
import me.winter.gdx.animation.scml.SCMLReader;
import net.jumpai.render.lighting.LightRenderMode;

import java.util.Locale;

/**
 * Reads a SCML project while supporting the custom lighting extension made for
 * Jumpaï. Allows to specify the light mode and extra assets such as normal map
 * and preshaded.
 * <p>
 * Created on 2020-10-01.
 *
 * @author Alexander Winter
 */
public class LightingSCMLReader extends SCMLReader
{
	@Override
	protected void loadAssets(Array<Element> folders)
	{
		for(Element folder : folders)
		{
			for(Element file : folder.getChildrenByName("file"))
			{
				String name = file.get("name");

				String[] parts = name.split("/");
				name = parts[parts.length - 1].replace(".png", "");

				LitTextureSpriteDrawable asset;

				TextureRegion region = getAtlas().findRegion(name);
				TextureRegion normal = null;
				TextureRegion preshaded = null;
				LightRenderMode mode = LightRenderMode.STANDARD;
				boolean usePreshaded = true;

				if(file.hasAttribute("normal"))
				{
					String normalName = file.get("normal");

					String[] normalParts = normalName.split("/");
					normalName = normalParts[normalParts.length - 1].replace(".png", "");

					normal = getAtlas().findRegion(normalName);

				}

				if(file.hasAttribute("preshaded"))
				{
					String preshadedName = file.get("preshaded");

					String[] preshadedParts = preshadedName.split("/");
					preshadedName = preshadedParts[preshadedParts.length - 1].replace(".png", "");

					preshaded = getAtlas().findRegion(preshadedName);
				}

				if(file.hasAttribute("mode"))
					try
					{
						mode = LightRenderMode.valueOf(file.get("mode").toUpperCase(Locale.US));
					}
					catch(IllegalArgumentException ignored) {}

				if(file.hasAttribute("usePreshaded"))
					usePreshaded = file.getBoolean("usePreshaded");

				asset = new LitTextureSpriteDrawable(region,
						normal,
						preshaded,
						file.getFloat("pivot_x", 0f),
						file.getFloat("pivot_y", 1f),
						mode,
						usePreshaded);

				currentProject.putAsset(folder.getInt("id"), file.getInt("id"), asset);
			}
		}
	}
}
