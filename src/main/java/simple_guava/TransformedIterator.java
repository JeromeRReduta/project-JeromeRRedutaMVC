package simple_guava;

import java.util.Iterator;

/**
 * An iterator that transforms a backing iterator. next() returns a transformed
 * version of some backing iterator's next(). Based off the guava implementation,
 * but very pared down.
 * @author JRRed
 *
 */
abstract class TransformedIterator <F extends Object, T extends Object>
	implements Iterator<T> {
	final Iterator<? extends F> backingIterator;
	
	TransformedIterator(Iterator<? extends F> backingIterator) {
		this.backingIterator = backingIterator;
	}
	
	/**
	 * Transforms an element from the backing iterator
	 * @param element
	 * @return
	 */
	abstract T transform(F element);
	
	@Override
	public final boolean hasNext() {
		return backingIterator.hasNext();
	}
	
	@Override
	public final T next() {
		F next = backingIterator.next();
		return transform(next);
	}
	
	@Override
	public final void remove() {
		backingIterator.remove();
	}
}
