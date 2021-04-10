package net.jumpai.util.ui.drawable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.jumpai.util.FloatSupplier;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Wrapper drawable that shifts the hue of its input drawable when drawing
 * <p>
 * Created on 2019-09-05.
 *
 * @author Alexander Winter
 */
public class HueShiftedDrawable extends WrapperDrawable
{
	private final FloatSupplier hueShiftSupplier;

	public HueShiftedDrawable(Drawable drawable, float hueShift)
	{
		this(drawable, () -> hueShift);
	}

	public HueShiftedDrawable(Drawable drawable, FloatSupplier hueShiftSupplier)
	{
		super(drawable);
		ensureNotNull(hueShiftSupplier, "hueShiftSupplier");
		this.hueShiftSupplier = hueShiftSupplier;
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height)
	{
		//batch.getShader().setUniformf("u_hueShift", hueShiftSupplier.getAsFloat());
		super.draw(batch, x, y, width, height);
		//batch.getShader().setUniformf("u_hueShift", 0f);
	}
}
