package de.jjco.gui;

/**
 * This interface is meant for users so that they can receive 
 * events such as button presses in their applications.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 * @see Gui
 */
public interface GuiEventReceiver {
	/**
	 * This method is called when a Gui tied to this event
	 * receiver does something worthy of attention (like getting
	 * pressed).
	 * 
	 * @param g the Gui object that sent the event
	 * @param det the event details, expressed in a string
	 */
	public void receivedEvent(Gui g, String det);
}
