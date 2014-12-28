package de.jjco.bounding;

/**
 * This class acts as a middle-man to translate data over from any virtual object to a
 * bounding object.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 * @param <K> the object to be translated
 * @param <V> the bounding object to get the translation
 */
public abstract class TranslationHandler<K extends Object, V extends BoundingObject> {
	/**
	 * Translates data from the generic object to the bounding object.
	 * 
	 * @param obj the generic object
	 * @param bo the bounding object
	 */
	public abstract void translate(K obj, V bo);
}
