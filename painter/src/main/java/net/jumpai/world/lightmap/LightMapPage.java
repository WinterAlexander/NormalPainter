package net.jumpai.world.lightmap;

import net.jumpai.CustomSerializable;
import net.jumpai.world.component.render.RenderPriority;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static net.jumpai.util.io.StreamUtil.readByte;
import static net.jumpai.util.io.StreamUtil.writeByte;
import static net.jumpai.world.lightmap.LightMap.LIGHT_MAP_PAGE_TILE_COUNT;

/**
 * Page of a light map, discretization unit that holds a part of the light map
 * Size is defined by LIGHT_MAP_PAGE_SIZE
 * <p>
 * Created on 2020-07-30.
 *
 * @author Alexander Winter
 */
public class LightMapPage implements CustomSerializable
{
	private final byte[][] lightLevel = new byte[LIGHT_MAP_PAGE_TILE_COUNT][LIGHT_MAP_PAGE_TILE_COUNT];
	private final byte[][] obsType = new byte[LIGHT_MAP_PAGE_TILE_COUNT][LIGHT_MAP_PAGE_TILE_COUNT];

	public void set(LightMapPage page)
	{
		for(int i = 0; i < LIGHT_MAP_PAGE_TILE_COUNT; i++)
			for(int j = 0; j < LIGHT_MAP_PAGE_TILE_COUNT; j++)
			{
				lightLevel[i][j] = page.lightLevel[i][j];
				obsType[i][j] = page.obsType[i][j];
			}
	}

	public float getLightLevel(int tileX, int tileY)
	{
		return (lightLevel[tileX][tileY] & 0xFF) / 255f;
	}

	public void setLightLevel(float value, int tileX, int tileY)
	{
		lightLevel[tileX][tileY] = (byte)(value * 255f);
	}

	public RenderPriority getObstructionType(int tileX, int tileY)
	{
		return RenderPriority.fromId(obsType[tileX][tileY] & 0xFF);
	}

	public void setObstructionType(RenderPriority priority, int tileX, int tileY)
	{
		obsType[tileX][tileY] = (byte)priority.ordinal();
	}

	@Override
	public void readFrom(InputStream input) throws IOException
	{
		for(int i = 0; i < LIGHT_MAP_PAGE_TILE_COUNT; i++)
			for(int j = 0; j < LIGHT_MAP_PAGE_TILE_COUNT; j++)
			{
				lightLevel[i][j] = readByte(input);
				obsType[i][j] = readByte(input);
				RenderPriority.fromId(obsType[i][j] & 0xFF); // makes sure its valid
			}
	}

	@Override
	public void writeTo(OutputStream output) throws IOException
	{
		for(int i = 0; i < LIGHT_MAP_PAGE_TILE_COUNT; i++)
			for(int j = 0; j < LIGHT_MAP_PAGE_TILE_COUNT; j++)
			{
				writeByte(output, lightLevel[i][j]);
				writeByte(output, obsType[i][j]);
			}
	}
}
