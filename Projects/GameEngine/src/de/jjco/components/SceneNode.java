package de.jjco.components;

/**
 * This class is a subset of CompNode that allows
 * transitioning between two scenes.  When 
 * Window.switchScenes is called, the following
 * functions are called in a separate thread:
 * 	old.transitionOut
 * 	old.stop
 * 	new.initialize
 * 	new.transitionIn
 * 	new.start
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class SceneNode extends CompNode {
	/**
	 * The first function called when this node is 
	 * set as the root.  This function should contain
	 * functions that create all child nodes.
	 */
	public void initialize() {}
	
	/**
	 * The function that is called last.  All child
	 * nodes should be destroyed here.
	 */
	public void stop() {}
	
	/**
	 * The final function called when this node is
	 * set as the root.
	 */
	public void start() {}
	
	/**
	 * The function that is called when this node is 
	 * supposed to transition in, if applicable.
	 */
	public void transitionIn() {}
	
	/**
	 * The function that is called when this node is 
	 * supposed to transition out, if applicable.
	 */
	public void transitionOut() {}
}
