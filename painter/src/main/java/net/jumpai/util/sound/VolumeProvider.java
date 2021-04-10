package net.jumpai.util.sound;

/**
 * Provides volume
 * <p>
 * Created on 2018-02-04.
 *
 * @author Alexander Winter
 */
@FunctionalInterface
public interface VolumeProvider
{
	float getVolume();
}
