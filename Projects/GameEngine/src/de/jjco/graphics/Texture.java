package de.jjco.graphics;

import de.jjco.resources.GLResource;

/**
 * The texture class is an abstract class that allows for use of deferred textures.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public abstract class Texture extends GLResource {
	protected int textureID = -1;
	protected int width;
	protected int height;
	
	protected Texture() {
		
	}
	
	/**
	 * @return the texture ID used by the rendering engine
	 */
	public int getID() {
		return ( textureID );
	}
	
	/**
	 * @return the texture width
	 */
	public int getWidth() {
		return ( width );
	}
	
	/**
	 * @return the texture height
	 */
	public int getHeight() {
		return ( height );
	}
	
	protected void doDestroy() {
		textureID = -1;
	}
	
	protected void doLoad() {}
}
