package com.normalpainter.app.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.normalpainter.app.NormalPainterScreen;
import com.normalpainter.render.AssetSupplier;

import static com.normalpainter.util.Validation.ensureNotNull;

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
	}
}
