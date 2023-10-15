package com.normalpainter.lightmap;

import com.normalpainter.component.render.RenderPriority;
import com.winteralexander.gdx.utils.io.Serializable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.winteralexander.gdx.utils.io.StreamUtil.readByte;
import static com.winteralexander.gdx.utils.io.StreamUtil.writeByte;

/**
 * Page of a light map, discretization unit that holds a part of the light map
 * Size is defined by LIGHT_MAP_PAGE_SIZE
 * <p>
 * Created on 2020-07-30.
 *
 * @author Alexander Winter
 */
public class LightMapPage implements Serializable
{
	private final byte[][] lightLevel = new byte[LightMap.LIGHT_MAP_PAGE_TILE_COUNT][LightMap.LIGHT_MAP_PAGE_TILE_COUNT];
	private final byte[][] obsType = new byte[LightMap.LIGHT_MAP_PAGE_TILE_COUNT][LightMap.LIGHT_MAP_PAGE_TILE_COUNT];

	public void set(LightMapPage page)
	{
		for(int i = 0; i < LightMap.LIGHT_MAP_PAGE_TILE_COUNT; i++)
			for(int j = 0; j < LightMap.LIGHT_MAP_PAGE_TILE_COUNT; j++)
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
		for(int i = 0; i < LightMap.LIGHT_MAP_PAGE_TILE_COUNT; i++)
			for(int j = 0; j < LightMap.LIGHT_MAP_PAGE_TILE_COUNT; j++)
			{
				lightLevel[i][j] = readByte(input);
				obsType[i][j] = readByte(input);
				RenderPriority.fromId(obsType[i][j] & 0xFF); // makes sure its valid
			}
	}

	@Override
	public void writeTo(OutputStream output) throws IOException
	{
		for(int i = 0; i < LightMap.LIGHT_MAP_PAGE_TILE_COUNT; i++)
			for(int j = 0; j < LightMap.LIGHT_MAP_PAGE_TILE_COUNT; j++)
			{
				writeByte(output, lightLevel[i][j]);
				writeByte(output, obsType[i][j]);
			}
	}
}
