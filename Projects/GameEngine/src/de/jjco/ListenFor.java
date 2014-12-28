package de.jjco;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation used for listening for events from the EventFactory.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 * @see EventFactory
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenFor {
    String value();
}
