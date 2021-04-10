package net.jumpai.tools.normalpainter;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import net.jumpai.client.ui.dialog.OkDialog;
import net.jumpai.render.AssetSupplier;

import java.util.function.Consumer;

/**
 * {@link OkDialog} without its force ignoreEvent listener
 * <p>
 * Created on 2020-12-21.
 *
 * @author Alexander Winter
 */
public class OkayDialog extends OkDialog
{
	public OkayDialog(AssetSupplier assets, String... text)
	{
		super(assets, text);
		clearListeners();
	}

	public OkayDialog(AssetSupplier assets, Consumer<Dialog> then, String... text)
	{
		super(assets, then, text);
		clearListeners();
	}
}
