package com.normalpainter.render;

import static com.normalpainter.util.Validation.ensureNotNull;

/**
 * Quality of textures used by Jumpaï, default and prior to having those
 * settings was always 4K. See Downscalator tool for generating lower
 * resolution textures from the 4K base point
 * <p>
 * Created on 2021-01-15.
 *
 * @author Alexander Winter
 */
public enum TextureQuality
{
	LOW("gfx_720", "texturequality_low"),
	HIGH("gfx_1080", "texturequality_high"),
	PERFECT("gfx_4k", "texturequality_perfect")
	;

	private final String dirName, nameKey;

	TextureQuality(String dirName, String nameKey)
	{
		ensureNotNull(dirName, "dirName");
		ensureNotNull(nameKey, "nameKey");
		this.dirName = dirName;
		this.nameKey = nameKey;
	}

	public String getDirName()
	{
		return dirName;
	}

	public String getNameKey()
	{
		return nameKey;
	}
}
