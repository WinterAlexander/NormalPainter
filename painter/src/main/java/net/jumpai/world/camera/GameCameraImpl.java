package net.jumpai.world.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static java.lang.Math.pow;
import static java.lang.StrictMath.signum;
import static net.jumpai.tools.normalpainter.NormalPainterScreen.MAX_ALLOWED_WORLD_SCALING;
import static net.jumpai.util.math.MathUtil.pow2;

/**
 * A camera for the WorldScreen, it gives a snapshot to the player by looking at where it is going
 * <p>
 * Created on 2016-11-28.
 *
 * @author Alexander Winter
 */
public class GameCameraImpl extends OrthographicCamera implements GameCamera
{
	private static final float MAX_HORIZONTAL_OFFSET = 8_000f;
	private static final float MAX_VERTICAL_OFFSET = 5_000f;

	/**
	 * This value represents how much the lerpSmoothing should have moved in a second relative to the actual position
	 */
	private static final float TRANS_RATE = 0.95f;
	private static final float TRANS_RATE_PER_FRAME = (float)pow(1f - TRANS_RATE, 1f / 60f);

	/**
	 * This value represents at what Y distance transitionRate is applied as if. If the camera is close to the actual
	 * position transition rate will lower while if far it will increase
	 */
	private static final float TRANS_RATE_Y_DIST = 2_000;

	/**
	 * Transition rate used when in a soft transition
	 */
	private static final float SOFT_TRANS_RATE = 0.05f;
	private static final float SOFT_TRANS_RATE_PER_FRAME = (float)pow(SOFT_TRANS_RATE, 1f / 60f);

	private static final float FOV_MIN = 1f / MAX_ALLOWED_WORLD_SCALING;

	private static final float SCREEN_SHAKE_INTENSITY = 0.02f;

	private CameraTarget target;
	private WorldBoundaries bounds;

	private final Vector2 tmpPosition = new Vector2();
	private final Vector2 tmpViewSize = new Vector2();

	/**
	 * Position of the GameCamera as returned by getPosition(), actual position
	 * of the OrthographicCamera differs from this one for zoom adjustments only.
	 */
	private final Vector2 logicalPosition = new Vector2();

	private boolean jump = false;

	private float softTrans = 0f;
	private float fovIncrease = 1f;

	private final boolean canShake;

	/**
	 * This position is a portion of the position the camera should be looking
	 * at and a portion of the previous position.
	 *
	 * Those portions are defined by transitionRate
	 */
	private float lerpSmoothing;

	public GameCameraImpl(boolean canShake)
	{
		this.canShake = canShake;
	}

	@Override
	public void tick(float delta)
	{
		if(target == null)
		{
			super.update();
			return;
		}

		if(jump)
		{
			setPosition(target.getTargetPosition());
			jump = false;
		}

		//95% per second to x% per frame
		float valueX = TRANS_RATE_PER_FRAME;

		//applying lerp-smoothing
		lerpSmoothing = lerpSmoothing * valueX + target.getTargetPosition().x * (1f - valueX);

		float projectedFocus;

		projectedFocus = target.getTargetPosition().x * 3 - lerpSmoothing * 2; //set it to current position
		projectedFocus = projectedFocus * (1f - softTrans) + lerpSmoothing * softTrans;
		softTrans *= SOFT_TRANS_RATE_PER_FRAME;

		logicalPosition.x = logicalPosition.x * valueX + projectedFocus * (1f - valueX); //portion of actual value and a portion of the projectedFocus

		float prevY = logicalPosition.y;
		//y transition rate isn't fixed at a constant but depends on the distance
		float valueY = (float)pow(TRANS_RATE_PER_FRAME, pow2(target.getTargetPosition().y - prevY) / pow2(TRANS_RATE_Y_DIST));

		logicalPosition.y = prevY * valueY + target.getTargetPosition().y * (1f - valueY);

		float dirX = signum(logicalPosition.x - target.getTargetPosition().x);
		float dirY = signum(logicalPosition.y - target.getTargetPosition().y);

		if(dirX * (logicalPosition.x - target.getTargetPosition().x) > MAX_HORIZONTAL_OFFSET)
			logicalPosition.x = target.getTargetPosition().x + dirX * MAX_HORIZONTAL_OFFSET;

		if(dirY * (logicalPosition.y - target.getTargetPosition().y) > MAX_VERTICAL_OFFSET)
			logicalPosition.y = target.getTargetPosition().y + dirY * MAX_VERTICAL_OFFSET;

		zoom /= fovIncrease;
		zoom *= (fovIncrease = FOV_MIN + (1f - FOV_MIN) * target.getFOVIncrease());

		replaceCamera(logicalPosition);

		position.set(logicalPosition, 0f);

		if(target.isScreenShaking() && canShake)
		{
			zoom *= (1f - SCREEN_SHAKE_INTENSITY);
			position.x += MathUtils.random(-SCREEN_SHAKE_INTENSITY * viewportWidth / 2 * fovIncrease, SCREEN_SHAKE_INTENSITY * viewportWidth / 2 * fovIncrease);
			position.y += MathUtils.random(-SCREEN_SHAKE_INTENSITY * viewportHeight / 2 * fovIncrease, SCREEN_SHAKE_INTENSITY * viewportHeight / 2 * fovIncrease);
			fovIncrease *= (1f - SCREEN_SHAKE_INTENSITY);
		}

		super.update();
	}

	public void setPosition(Vector2 pos)
	{
		setPosition(pos.x, pos.y);
	}

	public void setPosition(Vector3 pos)
	{
		setPosition(pos.x, pos.y);
	}

	/**
	 * Used to teleport the camera to a specified position
	 */
	public void setPosition(float x, float y)
	{
		logicalPosition.set(x, y);
		lerpSmoothing = x;

		replaceCamera(logicalPosition);

		position.set(logicalPosition, 0f);
	}

	private void replaceCamera(Vector2 pos)
	{
		if(bounds == null)
			return;

		float extendX = viewportWidth / 2;
		float extendY = viewportHeight / 2;

		if(pos.x > bounds.getMax().x - extendX)
			pos.x = bounds.getMax().x - extendX;

		if(pos.x < bounds.getMin().x + extendX)
			pos.x = bounds.getMin().x + extendX;

		if(pos.y > bounds.getMax().y - extendY)
			pos.y = bounds.getMax().y - extendY;

		if(pos.y < bounds.getMin().y + extendY)
			pos.y = bounds.getMin().y + extendY;
	}

	@Override
	public void jump()
	{
		jump = true;
	}

	@Override
	public void softTransition()
	{
		softTrans = 1f;
	}

	@Override
	public CameraTarget getTarget()
	{
		return target;
	}

	@Override
	public void setTarget(CameraTarget target)
	{
		this.target = target;
	}

	@Override
	public WorldBoundaries getBounds()
	{
		return bounds;
	}

	@Override
	public void setBounds(WorldBoundaries bounds)
	{
		this.bounds = bounds;
	}

	@Override
	public Vector2 getLogicalPosition()
	{
		return logicalPosition;
	}

	@Override
	public Vector2 getActualPosition()
	{
		tmpPosition.set(position.x, position.y);
		return tmpPosition;
	}

	@Override
	public Vector2 getViewSize()
	{
		tmpViewSize.set(viewportWidth, viewportHeight);
		return tmpViewSize;
	}

	@Override
	public float getLogicalZoom()
	{
		return 1f;
	}

	@Override
	public float getActualZoom()
	{
		return zoom;
	}
}
