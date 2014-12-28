package de.jjco.components;

/**
 * This class is a subclass of CompNode that
 * will draw itself onto the screen.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public abstract class DrawableNode extends CompNode {
	/**
	 * Called when the object needs to be drawn.
	 */
	public abstract void draw();
}
