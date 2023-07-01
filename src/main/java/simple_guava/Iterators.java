package simple_guava;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

public class Iterators {
	private enum EmptyModifiableIterator implements Iterator<Object> {
		/** Singleton pattern */
		INSTANCE;
		
		@Override
		public boolean hasNext() {
			return false;
		}
		
		@Override
		public Object next() {
			throw new NoSuchElementException();
		}
		
		@Override
		public void remove() {
			throw new IllegalStateException();
		}
	}
	
	@SuppressWarnings("unchecked")
	static <T extends Object> Iterator<T> emptyModifiableIterator() {
		return (Iterator<T>) EmptyModifiableIterator.INSTANCE;
	}

	static void clear(Iterator<?> iterator) {
		if (iterator == null) {
			return;
		}
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
	}
}
