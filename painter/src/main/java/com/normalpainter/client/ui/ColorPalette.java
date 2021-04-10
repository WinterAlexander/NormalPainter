package com.normalpainter.client.ui;

import com.badlogic.gdx.graphics.Color;

import static com.normalpainter.util.Validation.ensureNotNull;

/**
 * Palette of 5 colors used to color the UI of the entire game
 * <p>
 * Created on 2019-06-20.
 *
 * @author Alexander Winter
 */
public class ColorPalette
{
	private final Color alpha, beta, gamma, delta, omega;

	public ColorPalette(Color alpha,
	                    Color beta,
	                    Color gamma,
	                    Color delta,
	                    Color omega)
	{
		ensureNotNull(alpha, "alpha");
		ensureNotNull(beta, "beta");
		ensureNotNull(gamma, "gamma");
		ensureNotNull(delta, "delta");
		ensureNotNull(omega, "omega");

		this.alpha = alpha;
		this.beta = beta;
		this.gamma = gamma;
		this.delta = delta;
		this.omega = omega;
	}

	public Color getAlpha()
	{
		return alpha;
	}

	public Color getBeta()
	{
		return beta;
	}

	public Color getGamma()
	{
		return gamma;
	}

	public Color getDelta()
	{
		return delta;
	}

	public Color getOmega()
	{
		return omega;
	}
}
