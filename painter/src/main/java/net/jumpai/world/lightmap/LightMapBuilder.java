package net.jumpai.world.lightmap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LongMap;
import com.badlogic.gdx.utils.LongMap.Entry;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Queue;
import net.jumpai.world.World;
import net.jumpai.world.WorldObject;
import net.jumpai.world.camera.WorldBoundaries;
import net.jumpai.world.component.light.LightObstructor;
import net.jumpai.world.component.render.RenderPriority;

import java.util.Comparator;
import java.util.Objects;

import static java.lang.Math.round;
import static net.jumpai.util.Validation.ensureNotNull;
import static net.jumpai.world.lightmap.LightMap.LIGHT_MAP_TILE_SIZE;

/**
 * Builds {@link LightMap} given a {@link World}
 * <p>
 * Created on 2020-08-04.
 *
 * @author Alexander Winter
 */
public class LightMapBuilder
{
	protected final World world;
	protected final WorldBoundaries boundaries;

	protected final LongMap<Float> obsLevel = new LongMap<>();
	protected final LongMap<Float> lightLevel = new LongMap<>();
	protected final Vector2 tmpVector2 = new Vector2();

	protected final Pool<Node> nodePool = new Pool<Node>() {
		@Override
		protected Node newObject() {
			return new Node();
		}
	};

	protected final Queue<Node> tmpQueue = new Queue<>();
	protected final Array<Node> tmpArray = new Array<>();

	protected final Comparator<Node> comparator = (o1, o2) ->
			Float.compare(obsLevel.get(index(o1.x, o1.y), 0f), obsLevel.get(index(o2.x, o2.y), 0f));

	public LightMapBuilder(World world, WorldBoundaries boundaries)
	{
		ensureNotNull(world, "world");
		ensureNotNull(boundaries, "boundaries");
		this.world = world;
		this.boundaries = boundaries;
	}

	public LightMap build()
	{
		return build(new LightMap());
	}

	protected LightMap build(LightMap output)
	{
		long start = System.nanoTime();
		obsLevel.clear();
		lightLevel.clear();
		output.clear();

		for(WorldObject object : world.getObjects())
		{
			if(object instanceof LightObstructor)
			{
				LightObstructor obs = (LightObstructor)object;

				float halfStep = LIGHT_MAP_TILE_SIZE / 2f;
				output.addObstruction(RenderPriority.NULL,
						obs.getObsStartX(),
						obs.getObsStartY(),
						obs.getObsEndX() - obs.getObsStartX(),
						obs.getObsEndY() - obs.getObsStartY());

				int obsTileXStart = round(obs.getObsStartX() / LIGHT_MAP_TILE_SIZE);
				int obsTileXEnd = round(obs.getObsEndX() / LIGHT_MAP_TILE_SIZE);

				int obsTileYStart = round(obs.getObsStartY() / LIGHT_MAP_TILE_SIZE);
				int obsTileYEnd = round(obs.getObsEndY() / LIGHT_MAP_TILE_SIZE);

				for(int x = obsTileXStart; x < obsTileXEnd; x++)
					for(int y = obsTileYStart; y < obsTileYEnd; y++)
						if(obs.blocks(tmpVector2.set(x * LIGHT_MAP_TILE_SIZE + halfStep, y * LIGHT_MAP_TILE_SIZE + halfStep)))
						{
							long index = index(x, y);
							float newObs = 1f - (1f - obs.getObstructionLevel()) * (1f - obsLevel.get(index, 0f));

							obsLevel.put(index, newObs);
						}
			}
		}

		int minX = round(boundaries.getMin().x / LIGHT_MAP_TILE_SIZE);
		int maxX = round(boundaries.getMax().x / LIGHT_MAP_TILE_SIZE);
		int minY = round(boundaries.getMin().y / LIGHT_MAP_TILE_SIZE);
		int maxY = round(boundaries.getMax().y / LIGHT_MAP_TILE_SIZE);

		for(int outX = minX; outX < maxX; outX++)
			for(int outY = minY; outY < maxY; outY++)
			{
				long outIdx = index(outX, outY);

				if(lightLevel.containsKey(outIdx) || obsLevel.get(outIdx, 0f) != 0f)
					continue;

				Node first = nodePool.obtain();
				first.x = outX;
				first.y = outY;
				lightLevel.put(outIdx, 1f);
				tmpQueue.addLast(first);
			}


		while(tmpQueue.size > 0)
		{
			Node element = tmpQueue.removeFirst();

			int x = element.x;
			int y = element.y;
			long index = index(x, y);

			long leftIdx = index(x - 1, y);
			long rightIdx = index(x + 1, y);
			long downIdx = index(x, y - 1);
			long upIdx = index(x, y + 1);

			float leftObs = obsLevel.get(leftIdx, 0f);
			float rightObs = obsLevel.get(rightIdx, 0f);
			float downObs = obsLevel.get(downIdx, 0f);
			float upObs = obsLevel.get(upIdx, 0f);

			float light = lightLevel.get(index);

			nodePool.free(element);

			if(leftObs < 1f && !lightLevel.containsKey(leftIdx) && x - 1 >= minX)
			{
				Node left = nodePool.obtain();
				left.x = x - 1;
				left.y = y;
				lightLevel.put(leftIdx, light * (1f - leftObs));
				tmpArray.add(left);
			}

			if(rightObs < 1f && !lightLevel.containsKey(rightIdx) && x + 1 < maxX)
			{
				Node right = nodePool.obtain();
				right.x = x + 1;
				right.y = y;
				lightLevel.put(rightIdx, light * (1f - rightObs));
				tmpArray.add(right);
			}

			if(downObs < 1f && !lightLevel.containsKey(downIdx) && y - 1 >= minY)
			{
				Node down = nodePool.obtain();
				down.x = x;
				down.y = y - 1;
				lightLevel.put(downIdx, light * (1f - downObs));
				tmpArray.add(down);
			}

			if(upObs < 1f && !lightLevel.containsKey(upIdx) && y + 1 < maxY)
			{
				Node up = nodePool.obtain();
				up.x = x;
				up.y = y + 1;
				lightLevel.put(upIdx, light * (1f - upObs));
				tmpArray.add(up);
			}

			tmpArray.sort(comparator);
			for(Node e : tmpArray)
				tmpQueue.addLast(e);
			tmpArray.clear();
		}

		for(Entry<Float> entry : lightLevel)
		{
			int x = (int)entry.key;
			int y = (int)(entry.key >> 32);
			output.setLightLevel(entry.value, x, y);
		}

		long time = System.nanoTime() - start;
		float ms = time / 1_000_000f;

		world.getLogger().debug("LightMap built in " + ms + " ms");

		return output;
	}

	protected long index(int x, int y)
	{
		return (x & 0xFFFFFFFFL) | (((long)y) << 32);
	}

	protected static class Node
	{
		public int x, y;


		@Override
		public boolean equals(Object o)
		{
			if(this == o)
				return true;
			if(o == null || getClass() != o.getClass())
				return false;
			Node that = (Node)o;
			return x == that.x && y == that.y;
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(x, y);
		}
	}
}
