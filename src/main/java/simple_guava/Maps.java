package simple_guava;

import java.util.Map;

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

}
