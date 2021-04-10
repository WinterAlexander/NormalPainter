package net.jumpai.tools.normalpainter.jpun;

/**
 * Listens to the events of a JPen
 * <p>
 * Created on 2020-12-20.
 *
 * @author Alexander Winter
 */
public interface JPenListener
{
	void pressureChanged(float pressure);

	void tiltChanged(float x, float y);
}
