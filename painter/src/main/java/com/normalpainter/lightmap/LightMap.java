package com.normalpainter.lightmap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Entry;
import com.normalpainter.component.render.RenderPriority;
import com.winteralexander.gdx.utils.io.Serializable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;
import static com.winteralexander.gdx.utils.io.StreamUtil.readInt;
import static com.winteralexander.gdx.utils.io.StreamUtil.writeInt;
import static com.winteralexander.gdx.utils.math.MathUtil.negMod;

/**
 * Holds the back lighting intensity of a level for fast access
 * <p>
 * Created on 2020-07-30.
 *
 * @author Alexander Winter
 */
public class LightMap implements Serializable
{
	public static final int LIGHT_MAP_PAGE_TILE_COUNT = 360; // min for editor : 360

	public static final int LIGHT_MAP_TILE_SIZE = 250; // in world size
	public static final float LIGHT_MAP_PAGE_SIZE = LIGHT_MAP_PAGE_TILE_COUNT * LIGHT_MAP_TILE_SIZE; // must be > camera

	private final IntMap<LightMapPage> pages = new IntMap<>();

	private final Vector2 vectorOffset = new Vector2();

	public void set(LightMap other)
	{
		ensureNotNull(other, "other");
		pages.clear();
		for(Entry<LightMapPage> entry : other.pages)
		{
			LightMapPage copy = new LightMapPage();
			copy.set(entry.value);
			pages.put(entry.key, copy);
		}
	}

	@Override
	public void readFrom(InputStream input) throws IOException
	{
		clear();
		int size = readInt(input);
		pages.ensureCapacity(size);

		for(int i = 0; i < size; i++)
		{
			int pageId = readInt(input);
			LightMapPage page = new LightMapPage();
			page.readFrom(input);

			pages.put(pageId, page);
		}
	}

	@Override
	public void writeTo(OutputStream output) throws IOException
	{
		writeInt(output, pages.size);
		for(Entry<LightMapPage> page : pages)
		{
			writeInt(output, page.key);
			page.value.writeTo(output);
		}
	}

	public void clear()
	{
		pages.clear();
	}

	public float getLightLevel(int tileX, int tileY)
	{
		int pageX = Math.floorDiv(tileX, LIGHT_MAP_PAGE_TILE_COUNT);
		int pageY = Math.floorDiv(tileY, LIGHT_MAP_PAGE_TILE_COUNT);
		int offsetX = Math.floorMod(tileX, LIGHT_MAP_PAGE_TILE_COUNT);
		int offsetY = Math.floorMod(tileY, LIGHT_MAP_PAGE_TILE_COUNT);
		int id = getPageId(pageX, pageY);

		if(!pages.containsKey(id))
			return 0f;
		else
			return pages.get(id).getLightLevel(offsetX, offsetY);
	}

	public void setLightLevel(float level, int tileX, int tileY)
	{
		int pageX = Math.floorDiv(tileX, LIGHT_MAP_PAGE_TILE_COUNT);
		int pageY = Math.floorDiv(tileY, LIGHT_MAP_PAGE_TILE_COUNT);
		int offsetX = Math.floorMod(tileX, LIGHT_MAP_PAGE_TILE_COUNT);
		int offsetY = Math.floorMod(tileY, LIGHT_MAP_PAGE_TILE_COUNT);
		int id = getPageId(pageX, pageY);

		LightMapPage page;

		if(!pages.containsKey(id))
			pages.put(id, page = new LightMapPage());
		else
			page = pages.get(id);

		page.setLightLevel(level, offsetX, offsetY);
	}

	public RenderPriority getObstructionType(int tileX, int tileY)
	{
		int pageX = Math.floorDiv(tileX, LIGHT_MAP_PAGE_TILE_COUNT);
		int pageY = Math.floorDiv(tileY, LIGHT_MAP_PAGE_TILE_COUNT);
		int offsetX = Math.floorMod(tileX, LIGHT_MAP_PAGE_TILE_COUNT);
		int offsetY = Math.floorMod(tileY, LIGHT_MAP_PAGE_TILE_COUNT);
		int id = getPageId(pageX, pageY);

		LightMapPage page;

		if(!pages.containsKey(id))
			return RenderPriority.NULL;
		else
			page = pages.get(id);

		return page.getObstructionType(offsetX, offsetY);
	}

	public void setObstructionType(RenderPriority priority, int tileX, int tileY)
	{
		int pageX = Math.floorDiv(tileX, LIGHT_MAP_PAGE_TILE_COUNT);
		int pageY = Math.floorDiv(tileY, LIGHT_MAP_PAGE_TILE_COUNT);
		int offsetX = Math.floorMod(tileX, LIGHT_MAP_PAGE_TILE_COUNT);
		int offsetY = Math.floorMod(tileY, LIGHT_MAP_PAGE_TILE_COUNT);
		int id = getPageId(pageX, pageY);

		LightMapPage page;

		if(!pages.containsKey(id))
			pages.put(id, page = new LightMapPage());
		else
			page = pages.get(id);

		page.setObstructionType(priority, offsetX, offsetY);
	}

	public boolean isSourceBlocked(RenderPriority priority, float x, float y)
	{
		int tileX = Math.round(x / LIGHT_MAP_TILE_SIZE);
		int tileY = Math.round(y / LIGHT_MAP_TILE_SIZE);

		int pageX = Math.floorDiv(tileX, LIGHT_MAP_PAGE_TILE_COUNT);
		int pageY = Math.floorDiv(tileY, LIGHT_MAP_PAGE_TILE_COUNT);

		int offsetX = Math.floorMod(tileX, LIGHT_MAP_PAGE_TILE_COUNT);
		int offsetY = Math.floorMod(tileY, LIGHT_MAP_PAGE_TILE_COUNT);

		int id = getPageId(pageX, pageY);

		if(!pages.containsKey(id))
			return false;
		else
		{
			RenderPriority currentPriority = pages.get(id).getObstructionType(offsetX, offsetY);

			if(currentPriority == null)
				return false;

			return currentPriority.ordinal() > priority.ordinal();
		}
	}

	public void addObstruction(RenderPriority priority, float x, float y, float width, float height)
	{
		int startTileX = Math.round(x / LIGHT_MAP_TILE_SIZE);
		int startTileY = Math.round(y / LIGHT_MAP_TILE_SIZE);

		int endTileX = Math.round((x + width) / LIGHT_MAP_TILE_SIZE);
		int endTileY = Math.round((y + height) / LIGHT_MAP_TILE_SIZE);

		for(int tileX = startTileX; tileX < endTileX; tileX++)
			for(int tileY = startTileY; tileY < endTileY; tileY++)
			{
				RenderPriority current = getObstructionType(tileX, tileY);

				if(current != null && current.ordinal() > priority.ordinal())
					continue;

				setObstructionType(priority, tileX, tileY);
			}
	}

	public Vector2 getPageOffset(float x, float y)
	{
		return vectorOffset.set(negMod(x, LIGHT_MAP_PAGE_SIZE), negMod(y, LIGHT_MAP_PAGE_SIZE));
	}

	private int getPageId(int x, int y)
	{
		return (x & 0xFFFF) | ((y & 0xFFFF) << 16);
	}

	public LightMapPage getPage(int x, int y)
	{
		return pages.get(getPageId(x, y));
	}

	public void setPage(LightMapPage page, int x, int y)
	{
		pages.put(getPageId(x, y), page);
	}
}
