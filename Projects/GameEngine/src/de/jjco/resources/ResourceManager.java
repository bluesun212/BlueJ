package de.jjco.resources;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The ResourceManager is a large class intended to control
 * large groups of resources.  It serves three main purposes:
 * 1. To keep track of all resources that have been loaded,
 * so that one can get them later, as needed.
 * 2. To efficiently load groups of resources.  To accomplish
 * this, one may create resource "blocks", then can choose to
 * manipulate/load all resources in said block as a whole.
 * 3. To collect data about loading (which may be happening
 * in a different thread), like the amount of resources that
 * have been loaded, and the current resource that is being loaded.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 * @see GLResource, ResourceManager
 */
public class ResourceManager {
	private static volatile int resourceNumber;
	private static ConcurrentLinkedQueue<String> activeBlocks = new ConcurrentLinkedQueue<String>();
	private static ConcurrentHashMap<Integer, Resource> res = new ConcurrentHashMap<Integer, Resource>();
	private static ConcurrentHashMap<String, Resource> labels = new ConcurrentHashMap<String, Resource>();
	private static ConcurrentHashMap<String, ConcurrentLinkedQueue<Resource>> blocks = new ConcurrentHashMap<String, ConcurrentLinkedQueue<Resource>>();
	
	private static ConcurrentLinkedQueue<GLResource> glLoad = new ConcurrentLinkedQueue<GLResource>();
	private static ConcurrentLinkedQueue<GLResource> glDestroy = new ConcurrentLinkedQueue<GLResource>();
	
	private static ConcurrentLinkedQueue<Resource> loaded = new ConcurrentLinkedQueue<Resource>();
	private static Resource currentResource = null;
	
	/**
	 * Creates a resource.  This is called by Resource, so
	 * you don't need to call this yourself.
	 * 
	 * @param resource the resource to add
	 * @return the resource's ID
	 */
	public static int createResource(Resource resource) {
		int rid = resourceNumber++;
		res.put(rid, resource);
		
		for (String s : activeBlocks) {
			ConcurrentLinkedQueue<Resource> block = blocks.get(s);
			if (block == null) {
				block = new ConcurrentLinkedQueue<Resource>();
				blocks.put(s, block);
			}
			
			blocks.get(s).add(resource);
		}
		
		return ( rid );
	}
	
	/**
	 * Destroys a resource.  This is called by 
	 * Resource, so there is no need to call it.
	 * 
	 * @param rid the id of the resource
	 */
	public static void destroyResource(int rid) {
		if (res.containsKey(rid)) {
			Resource r = res.remove(rid);
			if (labels.containsValue(r)) {
				labels.remove(r.getName());
			}
			
			for (ConcurrentLinkedQueue<Resource> block : blocks.values()) {
				if (block.contains(r)) {
					block.remove(r);
				}
			}
		}
	}
	
	/**
	 * This caches the resource's name.  This is
	 * called by Resource, so there is no
	 * need to call it.
	 * 
	 * @param res the resource
	 */
	public static void nameResource(Resource res) {
		if (res.getName() != null) {
			labels.put(res.getName(), res);
		}
	}
	
	/**
	 * Gets a Resource based on its ID.
	 * 
	 * @param rid the id
	 * @return the resource, or null
	 */
	public static Resource getResourceByID(int rid) {
		return ( res.get(rid) );
	}
	
	/**
	 * Gets a Resource from its name.
	 * 
	 * @param name the name
	 * @return the resource, or null
	 */
	public static Resource getResourceByName(String name) {
		return (labels.get(name));
	}
	
	/**
	 * Blocks the current thread until the resource has
	 * loaded.
	 * 
	 * @param r the resource
	 */
	public static void blockUntilResourceLoads(Resource r) {
		while (!r.isLoaded() && !r.isDestroyed()) {
			try {
				Thread.sleep(1l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Block functions
	/**
	 * Starts a block with the specified name.
	 * All resources created from here until
	 * the end of the block will belong to the
	 * block.
	 * 
	 * @param id the unique name of the block
	 */
	public static void startResourceBlock(String id) {
		if ( !activeBlocks.contains(id) ) {
			activeBlocks.add(id);
		}
	}
	
	/**
	 * Ends a block with the specified name.
	 * 
	 * @param id the block's name
	 */
	public static void endResourceBlock(String id) {
		activeBlocks.remove(id);
	}
	
	/**
	 * Destroys all resources in a block.
	 * 
	 * @param id the block's name
	 */
	public static void destroyBlock(String id) {
		if ( isBlock(id) ) {
			for (Resource r : blocks.get(id)) {
				r.destroy();
			}
			
			blocks.remove(id);
		}
	}
	
	/**
	 * Loads all resources in the block.
	 * 
	 * @param id the block's name
	 */
	public static void loadBlock(String id) {
		if ( isBlock(id) ) {
			for (Resource r : blocks.get(id)) {
				currentResource = r;
				r.load();
			}
			
			currentResource = null;
		}
	}
	
	/**
	 * Checks to see if a block with the name exists.
	 * 
	 * @param id the block's name
	 * @return if the name corresponds to a valid block
	 */
	public static boolean isBlock(String id) {
		return ( blocks.containsKey(id) );
	}
	
	/**
	 * Checks to see if all the resources in the 
	 * block have been loaded.
	 * 
	 * @param id the block's name
	 * @return whether all resources have been loaded
	 */
	public static boolean isBlockLoaded(String id) {
		if (isBlock(id)) {
			for (Resource r : blocks.get(id)) {
				if (!r.isLoaded() && !r.isDestroyed()) {
					return (false);
				}
			}
			
			return (true);
		}
		
		return (false);
	}
	
	/**
	 * Gets the size of a block.
	 * 
	 * @param id the block's name
	 * @return the number of resources in a block
	 */
	public static int getResourcesInBlockSize(String id) {
		if (blocks.get(id) != null) {
			return ( blocks.get(id).size() );
		} else {
			return ( 0 );
		}
	}
	
	/**
	 * Gets the resources contained in the block.
	 * 
	 * @param id the block's name
	 * @return the resources in the block
	 */
	public static Resource[] getResourcesInBlock(String id) {
		if (blocks.get(id) != null) {
			Resource[] ret = new Resource[blocks.get(id).size()];
			return ( blocks.get(id).toArray(ret) );
		} else {
			return ( null );
		}
	}
	
	/**
	 * Blocks the current thread until all
	 * resources in the block load.
	 * 
	 * @param id the block's name.
	 */
	public static void blockUntilBlockLoads(String id) {
		while (!isBlockLoaded(id)) {
			try {
				Thread.sleep(1l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// All resources
	/**
	 * Destroys all the resources.
	 */
	public static void destroyAllResources() {
		for (Resource r : res.values()) {
			r.destroy();
		}
	}
	
	/**
	 * Loads all unloaded resources.
	 */
	public static void loadAllResources() {
		for (Resource r : res.values()) {
			currentResource = r;
			r.load();
		}
		
		currentResource = null;
	}

	// OpenGL Deferred loading
	/**
	 * Adds the resource to the loading queue.
	 * This method is called by GLResource, so
	 * it does not need to be called.
	 * 
	 * @param glr the GLResource
	 */
	public static void addToGLResourceLoadingQueue(GLResource glr) {
		glLoad.add(glr);
	}
	
	/**
	 * Loads and destroys all GLResources
	 * waiting in their respective queues.
	 */
	public static void doGLLoadingAndDestroying() {
		synchronized (glLoad) {
			for (GLResource glr : glLoad) {
				glr.loadWithGL();
				glLoad.remove(glr);
			}
		}
		
		synchronized (glDestroy) {
			for (GLResource glr : glDestroy) {
				glr.destroyWithGL();
				glDestroy.remove(glr);
			}
		}
	}

	/**
	 * Adds the resource to the destroying queue.
	 * This method is called by GLResource, so
	 * it does not need to be called.
	 * 
	 * @param glr the GLResource
	 */
	public static void addToGLResourceDestroyingQueue(GLResource glr) {
		glDestroy.add(glr);
	}
	
	// Loading data collection
	/**
	 * Called when the Resource has finished loading.
	 * This is called by Resource, so it does not need
	 * to be accessed.
	 * 
	 * @param id
	 */
	public static void finishLoading(Resource id) {
		if (getResourceByID(id.getResourceID()) != null) {
			loaded.add(id);
		}
	}
	
	/**
	 * Resets all loading information.  This should be done
	 * before every loading operation.
	 */
	public static void resetLoadingData() {
		loaded.clear();
		currentResource = null;
	}
	
	/**
	 * Gets the resources that have loaded so far.
	 * 
	 * @return the loaded resources
	 */
	public static Resource[] getLoadedResources() {
		Resource[] ret = new Resource[loaded.size()];
		return ( loaded.toArray(ret) );
	}
	
	/**
	 * Gets the number of resources that have been loaded so far.
	 * 
	 * @return the number of loaded resources
	 */
	public static int getLoadedResourcesSize() {
		return (loaded.size());
	}
	
	/**
	 * Gets the resource that is currently being loaded.
	 * 
	 * @return the loading resource
	 */
	public static Resource getCurrentResource() {
		return ( currentResource );
	}
}
