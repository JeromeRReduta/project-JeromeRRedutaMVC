package simple_guava;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

/**
 * VERY simple utilty methods for maps. Based on Guava's Maps class but incredibly simplified
 * @author JRRed
 *
 */
public final class Maps {
	
	private Maps() {}
	
	/**
	 * Gets a value from the map, returning null on any exceptions
	 * @param <V> v value
	 * @param map map
	 * @param key key
	 * @return if the mapping exists, returns the mapping. If no mapping exists, OR an exception
	 * would have occurred, returns null
	 */
	static <V extends Object> V safeGet(Map<?, V> map, Object key) {
		try {
			return map.get(key);
		}
		catch (ClassCastException | NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * Checks if a value exists in the map, returning false on any exceptions
	 * @param map map
	 * @param key key
	 * @return if the mapping exists, returns the mapping. If no mapping exists, OR an exception
	 * would have occurred, returns false
	 */
	static boolean safeContainsKey(Map<?, ?> map, Object key) {
		try {
			return map.containsKey(key);
		}
		catch (ClassCastException | NullPointerException e) {
			return false;
		}
	}
	
	/**
	 * Removes a value from the map, returning null on any exceptions
	 * @param <V> v value
	 * @param map map
	 * @param key key
	 * @return if the mapping exists, returns the mapping. If no mapping exists, OR an exception
	 * would have occurred, returns null
	 */
	static <V extends Object> V safeRemove(Map<?, V> map, Object key) {
		try {
			return map.remove(key);
		}
		catch (ClassCastException | NullPointerException e) {
			return null;
		}
	}
	
	abstract static class IteratorBasedAbstractMap
		<K extends Object, V extends Object>
		extends AbstractMap<K, V> {
		
		public abstract int size();
		
		abstract Iterator<Entry<K, V>> entryIterator();
		
		public Set<Entry<K, V>> entrySet() {
			return new EntrySet<K, V>() {
				@Override
				Map<K, V> map() {
					return IteratorBasedAbstractMap.this;
				}
				
				@Override
				public Iterator<Entry<K, V>> iterator() {
					return entryIterator();
				}
			};
			
		}
		
		public void clear() {
			Iterators.clear(entryIterator());
		}
	}
	
	abstract static class EntrySet<K extends Object, V extends Object>
		extends Sets.ImprovedAbstractSet<Entry<K, V>> {
		
		abstract Map<K, V> map();
		
		public int size() {
			return map().size();
		}
		
		public void clear() {
			map().clear();
		}
		
		public boolean contains(Object o) {
			if (o == null || !(o instanceof Entry)) {
				return false;
			}
			Entry<?, ?> entry = (Entry<?, ?>) o;
			Object key = entry.getKey();
			V value = Maps.safeGet(map(), key);
			return Objects.equals(value, entry.getValue())
					&& (value != null || map().containsKey(key));
		}
		
		public boolean isEmpty() {
			return map().isEmpty();
		}
		
		public boolean remove(Object o) {
			if (contains(o) && o instanceof Entry) {
				Entry<?, ?> entry = (Entry<?, ?>) o;
				var key = entry.getValue();
				return map().keySet().remove(key);
			}
			return false;
		}
		
		public boolean removeAll(Collection<?> c) {
			if (c == null) {
				return false;
			}
			try {
				return super.removeAll(c);
			}
			catch (UnsupportedOperationException e) {
				return false; // normally this does Sets.removeAllImpl(this, c.iterator() but I see no reasons to implement that here
			}
		}
		
		public boolean retainAll(Collection<?> c) {
			if (c == null) {
				return false;
			}
			try {
				return super.retainAll(c);
			}
			catch (UnsupportedOperationException e) {
				return false; // just returning false here for simplicity's sake
			}
		}
		
		// TODO: Line 4316 in Maps
		
	}
	

	
	
	
}
