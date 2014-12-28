package de.jjco.gui;

import de.jjco.EventFactory;
import de.jjco.ListenFor;
import de.jjco.Window;

/**
 * A button is a GUI that can be clicked.  It also has methods
 * for enabling/disabling clicks, and for setting/getting
 * text.  Note that a button does not have any methods for
 * drawing itself.  Drawing must be done by the user in 
 * a subclass.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 * @see Gui
 */
public class GuiButton extends Gui {
	private boolean disabled = false;
	private String text = "";
	
	@Override
	public void onReparent() {
		if (getParent() == null) {
			EventFactory.removeListener(this);
		} else {
			EventFactory.addListener(this);
		}
	}
	
	/**
	 * Enables or disables the button from being clicked.
	 * 
	 * @param b whether the button is disabled
	 */
	public void setDisabled(boolean b) {
		disabled = b;
	}
	
	/**
	 * @return whether the button is disabled
	 */
	public boolean getDisabled() {
		return ( disabled );
	}
	
	/**
	 * @return the button's text
	 */
	public String getText() {
		return ( text ); 
	}
	 
	/**
	 * @param s the button's new text
	 */
	public void setText(String s) {
		text = s;
	}
	
	/**
	 * Listens for mouse clicks on the button if the button is enabled.
	 * If this succeeds, then the button sends out an event with the
	 * string "pressed"
	 * 
	 * @param b the button
	 */
	@ListenFor("mousePressed")
	public void onMouseEvent(Window w, int b) {
		if ( w.isTheTopNode(w.getInput().getCursorPosition(), this) ) {
			if ( !disabled && b == 0 ) { // Disabled
				getReciever().receivedEvent(this, "pressed");
			}
		}
	}
}
