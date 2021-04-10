package net.jumpai.util.ui.colorpicker;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Listener that allows editing the value of a {@link SquareSlider}
 * <p>
 * Created on 2019-06-13.
 *
 * @author Alexander Winter
 */
public class SquareSliderListener extends InputListener
{
	private final SquareSlider slider;

	public SquareSliderListener(SquareSlider slider)
	{
		ensureNotNull(slider, "slider");

		this.slider = slider;
	}

	@Override
	public boolean touchDown(InputEvent event,
	                         float x,
	                         float y,
	                         int pointer,
	                         int button)
	{
		if(button != Buttons.LEFT)
			return false;

		touchDragged(event, x, y, pointer);
		return true;
	}

	@Override
	public void touchDragged(InputEvent event,
	                         float x,
	                         float y,
	                         int pointer)
	{
		Vector2 max = slider.getMax();
		Vector2 min = slider.getMin();

		slider.setValue(x / slider.getWidth() * (max.x - min.x) + min.x,
				y / slider.getHeight() * (max.y - min.y) + min.y);
	}
}
