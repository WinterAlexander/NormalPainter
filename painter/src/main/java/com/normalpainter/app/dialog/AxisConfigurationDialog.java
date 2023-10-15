package com.normalpainter.app.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.normalpainter.app.NormalPainterScreen;
import com.normalpainter.render.AssetSupplier;
import com.normalpainter.util.ui.dynamic.DynamicLabel;
import com.normalpainter.util.ui.listener.ChangeAdapter;
import com.normalpainter.util.ui.listener.ClickAdapter;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * Undocumented :(
 * <p>
 * Created on 2021-04-14.
 *
 * @author Alexander Winter
 */
public class AxisConfigurationDialog extends Dialog
{
	private final NormalPainterScreen screen;

	public AxisConfigurationDialog(NormalPainterScreen screen, AssetSupplier assets)
	{
		super("", ensureNotNull(assets, "assets").getSkin());

		ensureNotNull(screen, "screen");
		this.screen = screen;

		pad(20f);

		Table left = new Table();

		CheckBox swapXY = new CheckBox(" Swap X and Y", getSkin(), "default-small");
		swapXY.setChecked(screen.colorPicker.joystickSwapXY);
		swapXY.addListener(new ChangeAdapter(() -> screen.colorPicker.joystickSwapXY = swapXY.isChecked()));

		CheckBox invertX = new CheckBox(" Invert X", getSkin(), "default-small");
		invertX.setChecked(screen.colorPicker.joystickInvertX);
		invertX.addListener(new ChangeAdapter(() -> screen.colorPicker.joystickInvertX = invertX.isChecked()));

		CheckBox invertY = new CheckBox(" Invert Y", getSkin(), "default-small");
		invertY.setChecked(screen.colorPicker.joystickInvertY);
		invertY.addListener(new ChangeAdapter(() -> screen.colorPicker.joystickInvertY = invertY.isChecked()));

		Slider radius = new Slider(0.001f, 1f, 0.001f, false, getSkin());
		radius.setValue(screen.colorPicker.joystickRadius);
		DynamicLabel radiusLabel = new DynamicLabel(() -> "Radius:\n" + screen.colorPicker.joystickRadius, getSkin(), "default-small");
		radius.addListener(new ChangeAdapter(() -> {
			screen.colorPicker.joystickRadius = radius.getValue();
			radiusLabel.update();
		}));

		getContentTable().defaults().left().expandX();

		getContentTable().add(swapXY).row();
		getContentTable().add(invertX).row();
		getContentTable().add(invertY).row();
		getContentTable().add(radiusLabel).row();
		getContentTable().add(radius).growX().row();

		TextButton ok = new TextButton("Ok", getSkin());
		ok.addListener(new ClickAdapter(this::remove));

		getButtonTable().add(ok).minWidth(100f);
	}
}
