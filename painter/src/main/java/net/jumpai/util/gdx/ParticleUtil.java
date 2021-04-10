package net.jumpai.util.gdx;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.utils.ObjectMap;

import static com.badlogic.gdx.math.MathUtils.cosDeg;
import static com.badlogic.gdx.math.MathUtils.sinDeg;
import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static net.jumpai.util.math.MathUtil.negMod;
import static net.jumpai.util.math.MathUtil.pow2;

/**
 * Utility class for LibGDX's particle manipulation
 * <p>
 * Created on 2018-02-12.
 *
 * @author Alexander Winter
 */
public class ParticleUtil
{
	private static final ObjectMap<ParticleEffect, Float> rotations = new ObjectMap<>();

	private ParticleUtil() {}

	public static void rotate(ParticleEffect effect, float angle)
	{
		if(angle == 0f)
			return;

		for(ParticleEmitter emitter : effect.getEmitters())
		{
			ScaledNumericValue angleVal = emitter.getAngle();

			angleVal.setHighMax(normAngle(angleVal.getHighMax() + angle));
			angleVal.setHighMin(normAngle(angleVal.getHighMin() + angle));
			angleVal.setLowMax(normAngle(angleVal.getLowMax() + angle));
			angleVal.setLowMin(normAngle(angleVal.getLowMin() + angle));

			ScaledNumericValue rot = emitter.getRotation();
			rot.setActive(true);

			rot.setHighMax(normAngle(rot.getHighMax() + angle));
			rot.setHighMin(normAngle(rot.getHighMin() + angle));
			rot.setLowMax(normAngle(rot.getLowMax() + angle));
			rot.setLowMin(normAngle(rot.getLowMin() + angle));

			if(!emitter.getXOffsetValue().isActive() && !emitter.getYOffsetValue().isActive())
				continue;

			emitter.getXOffsetValue().setActive(true);
			emitter.getYOffsetValue().setActive(true);

			float lengthMin = (float)sqrt(pow2(emitter.getXOffsetValue().getLowMin())
					+ pow2(emitter.getYOffsetValue().getLowMin()));

			float oldAngleMin = (float)toDegrees(atan2(emitter.getYOffsetValue().getLowMin() / lengthMin,
					emitter.getXOffsetValue().getLowMin() / lengthMin));

			emitter.getXOffsetValue().setLowMin(lengthMin * cosDeg(angle + oldAngleMin));
			emitter.getYOffsetValue().setLowMin(lengthMin * sinDeg(angle + oldAngleMin));

			float lengthMax = (float)sqrt(pow2(emitter.getXOffsetValue().getLowMax())
					+ pow2(emitter.getYOffsetValue().getLowMax()));

			float oldAngleMax = (float)toDegrees(atan2(emitter.getYOffsetValue().getLowMax() / lengthMax,
				emitter.getXOffsetValue().getLowMax() / lengthMax));

			emitter.getXOffsetValue().setLowMax(lengthMax * cosDeg(angle + oldAngleMax));
			emitter.getYOffsetValue().setLowMax(lengthMax * sinDeg(angle + oldAngleMax));
		}

		float oldRotation = rotations.containsKey(effect) ? rotations.get(effect) : 0f;
		rotations.put(effect, oldRotation + angle);
	}

	private static float normAngle(float angle)
	{
		return negMod(angle, 360f);
	}

	public static void setRotation(ParticleEffect effect, float angle)
	{
		float oldRotation = rotations.containsKey(effect) ? rotations.get(effect) : 0f;
		rotate(effect, normAngle(angle - oldRotation));
	}

	public static void resetRotation(ParticleEffect effect, ParticleEffect original)
	{
		int size = effect.getEmitters().size;

		if(size != original.getEmitters().size)
			return;

		for(int i = 0; i < size; i++)
		{
			ParticleEmitter emitter = effect.getEmitters().get(i);
			ParticleEmitter originalE = original.getEmitters().get(i);

			emitter.getXOffsetValue().load(originalE.getXOffsetValue());
			emitter.getYOffsetValue().load(originalE.getYOffsetValue());
			emitter.getRotation().load(originalE.getRotation());
			emitter.getAngle().load(originalE.getAngle());
		}
		rotations.put(effect, 0f);
	}
}
