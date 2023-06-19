package simple_guava;

import java.util.Collection;

/**
 * Provides static methods for working with Collections. Based on the Guava class but incredibly
 * pared down and with my own implementations.
 * @author JRRed
 *
 */
public final class Collections2 {
	
	private Collections2() {}
	
	static boolean safeContains(Collection<?> collection, Object object) {
		try {
			return collection.contains(object);
		}
		catch (ClassCastException | NullPointerException e) {
			return false;
		}
	}
	
	static boolean safeRemove(Collection<?> collection, Object object) {
		try {
			return collection.remove(object);
		}
		catch (ClassCastException | NullPointerException e) {
			return false;
		}
	}
}