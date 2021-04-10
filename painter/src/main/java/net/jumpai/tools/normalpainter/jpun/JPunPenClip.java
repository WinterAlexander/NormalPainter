package net.jumpai.tools.normalpainter.jpun;

import jpen.owner.PenClip;

import java.awt.Point;
import java.awt.geom.Point2D.Float;

/**
 * {@link PenClip} that takes the whole screen
 * <p>
 * Created on 2020-12-20.
 *
 * @author Alexander Winter
 */
public class JPunPenClip implements PenClip
{
	@Override
	public void evalLocationOnScreen(Point point) {}

	@Override
	public boolean contains(Float aFloat) { return true; }
}
