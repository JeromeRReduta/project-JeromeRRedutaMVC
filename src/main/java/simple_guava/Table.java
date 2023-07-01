package simple_guava;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 
 * <p>A table data structure, mapping R to C to V triplets. It can be interpreted as a 2D matrix, e.g.
 * "table[num0][num1] = value" </p>
 * 
 * <p>In the spirit of Software Dev, and understanding how the data structures we use work,
 * I'm writing this myself. This is purely for learning purposes, and in an professional
 * setting I would just import from Guava.</p>
 * 
 * <p>Definitely based this off the one from Guava, too. HEAVILY simplified, though.</p>
 * 
 * @param <R> row key type
 * @param <C> column key type
 * @param <V> value key type
 * 
 * @author JRRed
 *
 */
public interface Table<R extends Object, C extends Object, V extends Object> {
	
	/**
	 * Returns true if the table contains a mapping with the rowKey and columnKey
	 * @param rowKey key of row
	 * @param columnKey key of column
	 * @return if the table contains a mapping with the rowKey and columnKey
	 */
	boolean contains(Object rowKey, Object columnKey);
	
	/**
	 * Returns true if the table has a row with the given row key
	 * @param rowKey key of row
	 * @return if the table has a row with the given row key
	 */
	boolean containsRow(Object rowKey);
	
	/**
	 * Returns true if the table has a column with the given column key
	 * @param columnKey key of column
	 * @return if the table has a column with the given column key
	 */
	boolean containsColumn(Object columnKey);

	/**
	 * Returns true if the table contains a mapping with the given value
	 * @param value value
	 * @return whether the table contains a mapping with the given value
	 */
	boolean containsValue(Object value);
	
	/**
	 * Returns the value corresponding to the given row and column keys. If no mapping exists,
	 * returns null
	 * @param rowKey key of row
	 * @param columnKey key of column
	 * @return the value for table[rowKey][columnKey], or null if there is no mapping
	 */
	V get(Object rowKey, Object columnKey);
	
	/**
	 * Returns true if the table has no mappings
	 * @return whether the table has no mappings
	 */
	boolean isEmpty();
	
	/**
	 * Returns the number of row / column / value mappings in the table
	 * @return number of row / column / value mappings in the table
	 */
	int size();
	
	@Override
	boolean equals(Object obj);
	
	@Override
	int hashCode();

	/**
	 * Removes all mappings in this table
	 */
	void clear();
	
	/**
	 * Makes a mapping with the given keys. If there's an existing mapping with these keys,
	 * it's overwritten.
	 * @param rowKey key of row
	 * @param columnKey key of column
	 * @param value value to put into map
	 * @return the value previous associated with these keys. If none existed, returns null
	 */
	V put(R rowKey, C columnKey, V value);
	
	/**
	 * Removes the mapping (if any) mapped to these keys
	 * @param rowKey key of row
	 * @param columnKey key of column
	 * @return the removed value. If none previously existed, returns null
	 */
	V remove(Object rowKey, Object columnKey);
	
	/**
	 * Returns a view of all mappings with the given row key as columnKey to value pairs.
	 * Like Guava's implementation, changing this row changes the backing table.
	 * @param rowKey key of row
	 * @return a map of columnKey to value pairs
	 */
	Map<C, V> row(R rowKey);
	
	/**
	 * Returns a view of all mappings with the given column key as rowKey to value pairs.
	 * Like Guava's implementation, changing this row changes the backing table.
	 * @param columnKey key of column
	 * @return a map of rowKey to value pairs
	 */
	Map<R, V> column(C columnKey);
	
	/**
	 * Returns a set of all row/column/value triplets. This set will not support add() or addAll().
	 * Like Guava's implementation, changing this row changes the backing table.
	 * @return set of row/column/value triplets, as cells
	 */
	Set<Cell<R, C, V>> cellSet();
	
	/**
	 * Returns a set of row keys that have at least one value in the table. Like Guava's implementation, changing this row changes the backing table.
	 * @return set of row keys
	 */
	Set<R> rowKeySet();
	
	/**
	 * Returns a set of column keys that have at least one value in the table. Like Guava's implementation, changing this row changes the backing table.
	 * @return set of column keys
	 */
	Set<C> columnKeySet();
	
	
	/**
	 * Returns a collection of all values. This may return duplicates. Like Guava's implementation, changing this row changes the backing table.
	 * @return collection of values
	 */
	Collection<V> values();
	
	/**
	 * Returns a view that associates each row key with the corresponding columnKey to value maps.
	 * Like Guava's implementation, changing this row changes the backing table.
	 * @return a map view from each row key to (columnKey to value) maps
	 */
	Map<R, Map<C, V>> rowMap();
	
	/**
	 * Returns a view that associates each column key with the corresponding rowKey to value maps.
	 * Like Guava's implementation, changing this row changes the backing table.
	 * @return a map view from each columnKey to (rowKey to value) maps
	 */
	Map<C, Map<R, V>> columnMap();
	
	/**
	 * Row key to column key to value triplets, representing a mapping in the table
	 * @author JRRed
	 *
	 * @param <R> row type
	 * @param <C> column type
	 * @param <V> value type
	 */
	interface Cell<R extends Object, C extends Object, V extends Object> {
		
		/**
		 * Returns this cell's row key
		 * @return row key
		 */
		R getRowKey();
		
		/**
		 * Returns this cell's column key
		 * @return column key
		 */
		C getColumnKey();
		
		/**
		 * Returns this cell's value
		 * @return value
		 */
		V getValue();
		
		/**
		 * Compares an object with this cell for equality. Two cells are equal if they
		 * have the same row keys, column keys, and values
		 * @param obj another object
		 * @return whether the two objects are equal
		 */
		@Override
		boolean equals(Object obj);
		
		/**
		 * Returns the hash code of this cell, which is defined as Objects.hash(
		 * 	getRowKey(),
		 * 	getColumnKey(),
		 * 	getValue()).
		 * @return this cell's hash code
		 */
		@Override
		int hashCode();
	}	
}
