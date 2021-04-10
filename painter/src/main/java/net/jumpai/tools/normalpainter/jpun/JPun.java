package net.jumpai.tools.normalpainter.jpun;

import jpen.PButtonEvent;
import jpen.PKindEvent;
import jpen.PLevel.Type;
import jpen.PLevelEvent;
import jpen.PScrollEvent;
import jpen.Pen;
import jpen.PenDevice;
import jpen.PenManager;
import jpen.event.PenListener;
import jpen.owner.PenOwner;
import net.jumpai.util.event.ListenableImpl;

import java.util.Collection;

/**
 * Wrapper for JPen and it's graphical tablet functionalities
 * <p>
 * Created on 2020-12-20.
 *
 * @author Alexander Winter
 */
public class JPun extends ListenableImpl<JPenListener> implements PenListener
{
	private final PenOwner penOwner = new JPunPenOwner();
	private final PenManager penManager = new PenManager(penOwner);

	private Pen pen = null;

	public JPun()
	{
		//System.out.println(new StatusReport(penManager));
		penManager.pen.addListener(this);
	}

	public Collection<PenDevice> getDevices()
	{
		return penManager.getDevices();
	}

	@Override
	public void penKindEvent(PKindEvent pKindEvent)
	{

	}

	@Override
	public void penLevelEvent(PLevelEvent pLevelEvent)
	{
		if(pen == null)
			pen = pLevelEvent.pen;
	}

	@Override
	public void penButtonEvent(PButtonEvent pButtonEvent)
	{

	}

	@Override
	public void penScrollEvent(PScrollEvent pScrollEvent)
	{

	}

	@Override
	public void penTock(long l)
	{
		if(pen == null)
			return;

		trigger(lis -> lis.pressureChanged(pen.getLevelValue(Type.PRESSURE)));
		trigger(lis -> lis.tiltChanged(pen.getLevelValue(Type.TILT_X), pen.getLevelValue(Type.TILT_Y)));

		pen = null;
	}
}
