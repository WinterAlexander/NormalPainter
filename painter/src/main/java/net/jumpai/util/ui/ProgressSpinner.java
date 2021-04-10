package net.jumpai.util.ui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.jumpai.render.AssetSupplier;

import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.sin;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Progress Spinner that rotates to indicate a waiting period.
 * <p>
 * Created on 2018-03-06.
 *
 * @author Cedric Martens
 * @author Alexander Winter
 */
public class ProgressSpinner extends Image
{
	public ProgressSpinner(AssetSupplier assets)
	{
		this(new TextureRegionDrawable(assets.getTexture("gfx/menu/loading.png")));
	}

	public ProgressSpinner(Drawable drawable)
	{
		super(drawable);
		addAction(sequence(Actions.alpha(0), Actions.fadeIn(0.5f, Interpolation.fade)));
		addAction(forever(Actions.rotateBy(360f * 3, 4f, new Interpolation() {
			@Override
			public float apply(float a) {
				return (1.25f * a + sin(2 * PI * a) / 2 * PI) / 1.25f;
			}
		})));
	}

	@Override
	protected void sizeChanged()
	{
		setOrigin(getWidth() / 2.0f, getHeight() / 2.0f);
		super.sizeChanged();
	}

	@Override
	public float getPrefWidth()
	{
		return 100f;
	}

	@Override
	public float getPrefHeight()
	{
		return 100f;
	}
}
