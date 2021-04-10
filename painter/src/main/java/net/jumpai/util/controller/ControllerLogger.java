package net.jumpai.util.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import static java.lang.Math.abs;

/**
 * DebugInfos class to identify actions from a controller
 * <p>
 * Created on 2016-12-25.
 *
 * @author Alexander Winter
 */
public class ControllerLogger implements ControllerListener
{
	@Override
	public void connected(Controller controller)
	{
		System.out.println(controller.getName() + " logged in !");
	}

	@Override
	public void disconnected(Controller controller)
	{
		System.out.println(controller.getName() + " logged out !");
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode)
	{
		System.out.println(controller.getName() + " pressed down " + buttonCode);
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode)
	{
		System.out.println(controller.getName() + " pressed up " + buttonCode);
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value)
	{
		if(abs(value) < 0.1f)
			return false;

		System.out.println(controller.getName() + " moved axis " + axisCode + " to " + value);
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value)
	{
		System.out.println(controller.getName() + " moved pov " + povCode + " to " + value);
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value)
	{
		System.out.println(controller.getName() + " moved slider x " + sliderCode + " to " + value);
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value)
	{
		System.out.println(controller.getName() + " moved slider y " + sliderCode + " to " + value);
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value)
	{
		System.out.println(controller.getName() + " accelerometer " + accelerometerCode + " to " + value);
		return false;
	}
}
