package com.normalpainter.client.ui.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

import java.util.function.Consumer;

/**
 * Represents a definition for a button
 * <p>
 * Created on 2018-07-28.
 *
 * @author Alexander Winter
 * @author Cedric Martens
 */
public class ButtonDefinition
{
	public final String name;
	public final Consumer<Dialog> action;
	public final String styleName;

	public ButtonDefinition(String name)
	{
		this(name, diag -> {});
	}

	public ButtonDefinition(String name, Consumer<Dialog> action)
	{
		this(name, action, "default-small");
	}

	public ButtonDefinition(String name, Consumer<Dialog> action, String styleName)
	{
		this.name = name;
		this.action = action;
		this.styleName = styleName;
	}
}
