package simple_guava;

import java.util.AbstractSet;
import java.util.Collection;

/**
 * Based on the Guava implementation, but pared down. Utility methods relating to sets
 * @author JRRed
 *
 */
public class Sets {
	
	private Sets() {}
	
	abstract static class ImprovedAbstractSet<E extends Object> extends AbstractSet<E> {
		
		@Override
		public boolean retainAll(Collection<?> c) {
			if (c == null) {
				return false;
			}
			return super.retainAll(c);
		}
	}

}
