package de.jjco.gui;

import de.jjco.bounding.AxisAlignedBoundingBox;
import de.jjco.components.CollisionNode;
import de.jjco.components.DrawableNode;

/**
 * A GUI is a type of component that has a definite width and 
 * height.  GUIs also have receivers that can be set.  When set,
 * the GUI can send a message to it saying that something has
 * happened.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 * @see EventReceiver
 */
public class Gui extends DrawableNode implements GuiEventReceiver {
	private double width = 0;
	private double height = 0;
	private GuiEventReceiver rec = this;
	
	private AxisAlignedBoundingBox def;
	private CollisionNode node;
	
	public Gui() {
		def = new AxisAlignedBoundingBox();
		
		node = new CollisionNode(def);
		node.reparentTo(this);
	}
	
	/**
	 * @return the component's width
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * @param width the new width
	 */
	public void setWidth(double width) {
		this.width = width;
		
		if ( node.getBoundingObject().equals(def) ) {
			def.setWidth(width);
		}
	}

	/**
	 * @return the component's height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * @param height the new height
	 */
	public void setHeight(double height) {
		this.height = height;
		
		if ( node.getBoundingObject().equals(def) ) {
			def.setHeight(height);
		}
	}
	
	/**
	 * @param ger the new event receiver
	 */
	public void setReciever(GuiEventReceiver ger) {
		rec = ger;
	}
	
	/**
	 * @return this gui's receiver
	 */
	public GuiEventReceiver getReciever() {
		return ( rec );
	}
	
	@Override
	public void receivedEvent(Gui g, String det) {
		
	}

	@Override
	public void draw() {
		
	}
}
