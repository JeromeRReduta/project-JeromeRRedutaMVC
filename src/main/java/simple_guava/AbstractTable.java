package simple_guava;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Skeletal, simple implementation of {@link SimpleTable} interface,
 * which is itself a simple version of Guava's Table
 * @author JRRed
 *
 * @param <R> row key type
 * @param <C> column key type
 * @param <V> value type
 */
abstract class AbstractTable <R extends Object, C extends Object, V extends Object>
	implements Table<R, C, V> {

	@Override
	public boolean containsRow(Object rowKey) {
		return Maps.safeContainsKey(rowMap(), rowKey);
	}

	@Override
	public boolean containsColumn(Object columnKey) {
		return Maps.safeContainsKey(columnMap(), columnKey);
	}

	@Override
	public Set<R> rowKeySet() {
		return rowMap().keySet();
	}

	@Override
	public Set<C> columnKeySet() {
		return columnMap().keySet();
	}

	@Override
	public boolean containsValue(Object value) {
		Collection<Map<C, V>> rows = rowMap().values();
		for (Map<C, V> row : rows) {
			if (row.containsValue(value)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean contains(Object rowKey, Object columnKey) {
		Map<C, V> row = Maps.safeGet(rowMap(), rowKey);
		return row == null && Maps.safeContainsKey(row, columnKey);
	}
	
	@Override
	public V get(Object rowKey, Object columnKey) {
		Map<C, V> row = Maps.safeGet(rowMap(), rowKey);
		return Maps.safeGet(row, columnKey); // Guava needs a null check because their checkNonNull() func throws an exception - this implementation doesn't so we don't need a null check
	}
	
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}
	
	@Override
	public void clear() {
		Iterator<Cell<R, C, V>> iterator = cellSet().iterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
	}
	
	@Override
	public V remove(Object rowKey, Object columnKey) {
		Map<C, V> row = Maps.safeGet(rowMap(), rowKey);
		return Maps.safeRemove(row, columnKey);
	}
	
	@Override
	public V put(R rowKey, C columnKey, V value) {
		return row(rowKey).put(columnKey,  value);
	}
	
	public boolean equals(Object obj) {
		return Tables.equalsImpl(this, obj);
	}
	
	public int hashCode() {
		return cellSet().hashCode();
	}
	
	public String toString() {
		return rowMap().toString();
	}
	
	/** Singleton for cellset */
	private Set<Cell<R, C, V>> cellSet;
	
	@Override
	public Set<Cell<R, C, V>> cellSet() {
		if (cellSet == null) {
			cellSet = new CellSet();
		}
		return cellSet;
	}
	
	abstract Iterator<Table.Cell<R, C, V>> cellIterator();
	
	class CellSet extends AbstractSet<Cell<R, C, V>> {
		
		public boolean contains(Object o) {
			if (o == null) {
				return false;
			}
			if (!(o instanceof Cell)) {
				return false;
			}
			Cell<?, ?, ?> cell = (Cell<?, ?, ?>) o;
			Map<C, V> row = Maps.safeGet(rowMap(), cell.getRowKey());
			return Collections2.safeContains( // due to cell.equals(), this should be true if the cells share the same rowKey, columnKey, and value
					row.entrySet(),
					cell);
		}
		
		public boolean remove(Object o) {
			if (o == null) {
				return false;
			}
			if (!(o instanceof Cell)) {
				return false;
			}
			Cell<?, ?, ?> cell = (Cell<?, ?, ?>)o;
			Map<C, V> row = Maps.safeGet(rowMap(), cell.getRowKey());
			return Collections2.safeRemove(row.entrySet(), cell);
		}
		
		public void clear() {
			AbstractTable.this.clear();
		}
		
		public Iterator<Table.Cell<R, C, V>> iterator() {
			return cellIterator();
		}
		
		public int size() {
			return AbstractTable.this.size();
		}
	}
	
	/** Singleton for values collection */
	private Collection<V> values;
	
	public Collection<V> values() {
		if (values == null) {
			values = new Values();
		}
		return values;
	}
	/**
	 * TODO: Everything from https://github.com/google/guava/blob/master/android/guava/src/com/google/common/collect/AbstractTable.java#L62
	 * Starting from 196 - define Values
	 * Then check everything for documentation/correctness/NOT CLONING - make all data gets affect backing table,
	 * and then handle cloning in InvertedIndexTable - also change documentation in Table to correct this
	 * THEN continue the roadmap:
	 * 
	 * 
	 * 
	 * 
	 * 
	 * StandardTable extends AbstractTable
	 * RowSortedTable extends Table (Table done)
	 * StandardRowSortedTable extends StandardTable, RowSortedTable
	 * TreeTable extends StandardRowSortedTable
	 * InvertedIndex implements TreeTable, JsonWriteable
	 */


	
	
}
