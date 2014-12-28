package de.jjco.resources;

import de.jjco.Window;

/**
 * The GLResource is a direct subclass of Resource,
 * but allows resources to be loaded and unloaded
 * safely in the OpenGL thread.
 * 
 * The loading and destroying are each separated 
 * into two parts.  The non-GL part of this resource
 * will be loaded/destroyed as soon as load/destroy
 * is called.  However, the GL part of the load/destroy
 * will only be called immediately if load/destroy is
 * called in the GL thread.  If it is not, the Window
 * will eventually load/destroy the GL parts in bulk.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 * @see GLResource, ResourceManager, Window
 */
public abstract class GLResource extends Resource {
	private boolean destroyed = false;
	private boolean loaded = false;
	
	@Override
	public void load() {
		if (loaded) {
			return;
		}
		
		doLoad();
		
		try {
			if (Window.getCurrentWindow() != null) {
				loadWithGL();
			} else {
				ResourceManager.addToGLResourceLoadingQueue(this);
			}
		} catch (Exception e) {}
	}
	
	@Override
	public void destroy() {
		if (destroyed) {
			return;
		}
		
		doDestroy();
		
		try {
			if (Window.getCurrentWindow() != null) {
				destroyWithGL();
			} else {
				ResourceManager.addToGLResourceDestroyingQueue(this);
			}
		} catch (Exception e) {}
	}
	
	/**
	 * Destroys the portion of this resource that interacts with OpenGL.
	 */
	public void destroyWithGL() {
		doDestroyWithGL();
		ResourceManager.destroyResource(getResourceID());
		destroyed = true;
		loaded = false;
	}
	
	/**
	 * Loads the portion of this resource that interacts with OpenGL.
	 */
	public void loadWithGL() {
		doLoadWithGL();
		ResourceManager.finishLoading(this);
		loaded = true;
	}
	
	@Override
	public boolean isDestroyed() {
		return ( destroyed );
	}
	
	@Override
	public boolean isLoaded() {
		return ( loaded );
	}
	
	/**
	 * Called when the GL part of this resource is being destroyed.
	 */
	protected abstract void doDestroyWithGL();
	
	/**
	 * Called when the GL part of this resource is loading.
	 */
	protected abstract void doLoadWithGL();
}
