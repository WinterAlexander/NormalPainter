package net.jumpai.util.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

/**
 * Adapter for Controller Listener, allows implementation of only part of the methods
 * <p>
 * Created on 2018-12-04.
 *
 * @author Alexander Winter
 */
public interface ControllerAdapter extends ControllerListener
{
	@Override
	default void connected(Controller controller)
	{

	}

	@Override
	default void disconnected(Controller controller)
	{

	}

	@Override
	default boolean buttonDown(Controller controller, int buttonCode)
	{
		return false;
	}

	@Override
	default boolean buttonUp(Controller controller, int buttonCode)
	{
		return false;
	}

	@Override
	default boolean axisMoved(Controller controller, int axisCode, float value)
	{
		return false;
	}

	@Override
	default boolean povMoved(Controller controller, int povCode, PovDirection value)
	{
		return false;
	}

	@Override
	default boolean xSliderMoved(Controller controller, int sliderCode, boolean value)
	{
		return false;
	}

	@Override
	default boolean ySliderMoved(Controller controller, int sliderCode, boolean value)
	{
		return false;
	}

	@Override
	default boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value)
	{
		return false;
	}
}
