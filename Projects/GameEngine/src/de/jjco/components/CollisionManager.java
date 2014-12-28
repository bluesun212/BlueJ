package de.jjco.components;

import java.util.LinkedList;

import de.jjco.bounding.BoundingObject;
import de.jjco.bounding.IntersectionManager;

/**
 * This node checks any child collision nodes it has 
 * for collision with other nodes.  If the intersecting 
 * nodes match keys with the keys provided here, then
 * the collision manager will alert its handler.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class CollisionManager extends CompNode {
	private String[] desc;
	
	/**
	 * Creates a collision manager with the specified search
	 * keys.
	 * 
	 * @param keys the keys
	 */
	public CollisionManager(String[] keys) {
		desc = keys;
	}
	
	/**
	 * Gets the search keys.
	 * @return the keys
	 */
	public String[] getKeys() {
		return (desc);
	}
	
	/**
	 * Sets the search keys.
	 * @param keys the keys
	 */
	public void setKeys(String[] keys) {
		desc = keys;
	}
	
	@Override
	public void step() {
		LinkedList<CompNode> ret = new LinkedList<CompNode>();
		CollisionHandler handler = getHandler();
		
		for (CompNode node : getChildren()) {
			if (node instanceof CollisionNode) {
				BoundingObject bb = ((CollisionNode) node).getBoundingObject();
				
				for (CollisionNode cs : getWindow().getNodesThatMatch(desc)) {
					BoundingObject bb2 = cs.getBoundingObject();
					
					if (IntersectionManager.intersects(bb, bb2)) {
						CompNode obj = cs.getHandler();
						
						if (obj != null && !ret.contains(obj)) {
							ret.add(obj);
						}
					}
				}
			}
		}
		
		if (!ret.isEmpty()) {
			CompNode[] colls = new CompNode[ret.size()];
			ret.toArray(colls);
			handler.handleCollisions(colls);
		}
	}

	private CollisionHandler getHandler() {
		CompNode child = getParent();
		
		while (child != null && !(child instanceof CollisionHandler)) {
			child = child.getParent();
		}
		
		return ((CollisionHandler) child);
	}
}
