package de.jjco;

import org.lwjgl.opengl.ARBMultisample;
import org.lwjgl.opengl.GL11;

import de.jjco.components.CompNode;
import de.jjco.components.DrawableNode;

/**
 * An interface describing the way a window should interact with OpenGL. 
 * The Window class itself does not interact with any core OpenGL functions
 * at all, in order to bme more modular.  This is useful in case one wanted
 * to create a subclass of the WindowSetup that worked on the new OpenGL
 * functions.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public abstract class WindowSetup {
	/**
	 * Sets up the window, calling any initializing OpenGL functions.
	 */
	public abstract void setupWindow();
	
	/**
	 * Calls any OpenGL functions before starting the render.
	 */
	public abstract void setupDraw();
	
	/**
	 * Destroys the window.
	 */
	public abstract void destroyWindow();
	
	/**
	 * Renders the window starting at this node.
	 * 
	 * @param sn the root node to render from
	 */
	public abstract void renderSceneNode(CompNode sn);
	
	static class WindowSetupImpl extends WindowSetup {
		@Override
		public void setupWindow() {
			GL11.glDisable(GL11.GL_DEPTH_TEST);      
			
			GL11.glDisable(ARBMultisample.GL_MULTISAMPLE_ARB);    
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}

		@Override
		public void setupDraw() {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0, 0, 0, 1);
			GL11.glClearDepth(1.0);
		}

		@Override
		public void destroyWindow() {
			
		}

		@Override
		public void renderSceneNode(CompNode sn) {
			GL11.glPushMatrix();
			GL11.glTranslated(sn.getX(), sn.getY(), 0);
			GL11.glRotated(sn.getAngle(), 0, 0, 1);
			
			if (sn instanceof DrawableNode) {
				((DrawableNode) sn).draw();
			}
			
			for (CompNode child : sn.getChildren()) {
				renderSceneNode(child);
			}
			
			GL11.glPopMatrix();
		}

	}
}
