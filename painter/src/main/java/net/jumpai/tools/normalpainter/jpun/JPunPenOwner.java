package net.jumpai.tools.normalpainter.jpun;

import jpen.PenProvider.Constructor;
import jpen.owner.AbstractPenOwner;
import jpen.owner.PenClip;
import jpen.owner.PenOwner;
import jpen.provider.osx.CocoaProvider;
import jpen.provider.wintab.WintabProvider;
import jpen.provider.xinput.XinputProvider;

import java.util.Arrays;
import java.util.Collection;

/**
 * Custom {@link PenOwner} for our JPen implementation
 * <p>
 * Created on 2020-12-20.
 *
 * @author Alexander Winter
 */
public class JPunPenOwner extends AbstractPenOwner
{
	private final PenClip penClip = new JPunPenClip();

	@Override
	protected void draggingOutDisengaged()
	{

	}

	@Override
	protected void init()
	{
		penManagerHandle.setPenManagerPaused(false);
	}

	@Override
	public Collection<Constructor> getPenProviderConstructors()
	{
		return Arrays.asList(
				new XinputProvider.Constructor(),
				new WintabProvider.Constructor(),
				new CocoaProvider.Constructor());
	}

	@Override
	public PenClip getPenClip()
	{
		return penClip;
	}
}
