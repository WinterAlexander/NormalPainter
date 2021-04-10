package net.jumpai.world.component.render;

/**
 * The priority in which world objects are rendered. Each object has its own
 * priority. Reordering of the enum constants will affect render priority. Top
 * is rendered first.
 * <p>
 * Created on 2018-05-17.
 *
 * @author Alexander Winter
 */
public enum RenderPriority
{
	NULL,
	BACKGROUND,
	BORDER, //editor only

	LOG_WALL(true),
	WALL,

	TREE,
	BUSH,

	GATE,

	LIQUIDFALL,

	SIGN,
	ARROW,

	BED,
	CHAIR,
	DOOR,

	LIQUIDFALL_PLATFORM,

	SPIKE,
	BEARTRAP,
	SAW,
	ELECTRICBARRIER,

	WOOD_STAIRS,
	PLATFORM,
	LAND,
	ICE_BLOCK,
	BLOCK,
	LOG_BLOCK(true),

	ONEWAY,

	ELECTRICSWITCH,
	FAN,

	CHECKPOINT,
	FINISHFLAG,

	MUSHROOM,
	PORTAL,
	MAGICCARPET,

	REMOTE_PLAYER,
	LOCAL_PLAYER,

	BOOMERANG,
	FRISBEE,
	KEY,

	EGG,

	CANNON,

	UMBRELLA,

	TABLE,

	LIQUID,
	WATERFALL_SMOKE,

	BUBBLE,
	CLOUD,
	;

	public boolean reverseY;

	RenderPriority()
	{
		this(false);
	}

	RenderPriority(boolean reverseY)
	{
		this.reverseY = reverseY;
	}

	public boolean reverseY()
	{
		return reverseY;
	}

	public static RenderPriority fromId(int id)
	{
		return values()[id];
	}
}
