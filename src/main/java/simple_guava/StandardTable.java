package simple_guava;

import java.util.Collection;

import static simple_guava.Maps.safeContainsKey;
import static simple_guava.Maps.safeGet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

import simple_guava.Maps.IteratorBasedAbstractMap;
import simple_guava.Sets.ImprovedAbstractSet;

import java.util.LinkedHashMap;

/**
 * Implementation of Guava's StandardTable. Credit to the original author.
 * @author JRRed
 *
 * @param <R> Row key
 * @param <C> Column key
 * @param <V> Value key
 */
class StandardTable<R, C, V> extends AbstractTable<R, C, V> {
	
	final Map<R, Map<C, V>> backingMap;
	final Supplier<? extends Map<C, V>> factory;
	
	StandardTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
		this.backingMap = backingMap;
		this.factory = factory;
	}
	
	public boolean contains(Object rowKey, Object columnKey) {
		return rowKey != null
				&& columnKey != null
				&& super.contains(rowKey, columnKey);
	}
	
	public boolean containsColumn(Object columnKey) {
		if (columnKey == null) {
			return false;
		}
		Collection<Map<C, V>> rows = backingMap.values();
		for (Map<C, V> map : rows) {
			if (safeContainsKey(map, columnKey)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsRow(Object rowKey) {
		return rowKey != null
				&& safeContainsKey(backingMap, rowKey);
	}
	
	public boolean containsValue(Object value) {
		return value != null
				&& super.containsValue(value);
	}
	
	public V get(Object rowKey, Object columnKey) {
		return (rowKey == null || columnKey == null)
				? null
				: super.get(rowKey, columnKey);
	}
	
	public boolean isEmpty() {
		return backingMap.isEmpty();
	}
	
	public int size() {
		int size = 0;
		Collection<Map<C, V>> columns = backingMap.values();
		for (Map<C, V> column : columns) {
			size += column.size();
		}
		return size;
	}
	
	public void clear() {
		backingMap.clear();
	}
	
	private Map<C, V> getOrCreate(R rowKey) {
		backingMap.putIfAbsent(
				rowKey,
				factory.get());
		return backingMap.get(rowKey);
	}
	
	public V get(R rowKey, C columnKey, V value) {
		if (rowKey == null || columnKey == null || value == null) {
			return null;
		}
		return getOrCreate(rowKey).put(columnKey, value);
	}
	
	public V remove(Object rowKey, Object columnKey) {
		if (rowKey == null || columnKey == null) {
			return null;
		}
		Map<C, V> map = safeGet(backingMap, rowKey);
		if (map == null) {
			return null;
		}
		V value = map.remove(columnKey);
		if (map.isEmpty()) {
			backingMap.remove(rowKey);
		}
		return value;
	}
	
	private Map<R, V> removeColumn(Object column) {
		Map<R, V> output = new LinkedHashMap<>();
		Iterator<Entry<R, Map<C, V>>> iterator = backingMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<R, Map<C, V>> entry = iterator.next();
			V value = entry.getValue().remove(column); // remove each column / value mapping that matches with our column parameter
			if (value != null) {
				output.put(entry.getKey(), value); // add row / value entry to output map
				if (entry.getValue().isEmpty()) { // if the row is empty after removing this column / value mapping, remove the row, too
					iterator.remove();
				}
			}
		}
		return output;
	}
	
	private boolean containsMapping(Object rowKey, Object columnKey, Object value) {
		return value != null
				&& value.equals(get(rowKey, columnKey));
	}
	
	private boolean removeMapping(Object rowKey, Object columnKey, Object value) {
		if (containsMapping(rowKey, columnKey, value)) {
			remove(rowKey, columnKey);
			return true;
		}
		return false;
	}
	
	private abstract class TableSet<T> extends ImprovedAbstractSet<T> {
		@Override
		public boolean isEmpty() {
			return backingMap.isEmpty();
		}
		
		@Override
		public void clear() {
			backingMap.clear();
		}
	}
	
	public Set<Cell<R, C, V>> cellSet() {
		return super.cellSet();
	}
	
	Iterator<Cell<R, C, V>> cellIterator() {
		return new CellIterator();
	}
	
	private class CellIterator implements Iterator<Cell<R, C, V>> {
		final Iterator<Entry<R, Map<C, V>>> rowIterator = backingMap.entrySet().iterator();
		Entry<R, Map<C, V>> rowEntry;
		Iterator<Entry<C, V>> columnIterator = Iterators.emptyModifiableIterator();
		@Override
		public boolean hasNext() {
			return rowIterator.hasNext()
					|| columnIterator.hasNext();
		}
		
		@Override
		public Cell<R, C, V> next() {
			if (!columnIterator.hasNext()) {
				rowEntry = rowIterator.next();
				columnIterator = rowEntry
						.getValue()
						.entrySet()
						.iterator();
			}
			Entry<C, V> columnEntry = columnIterator.next();
			return Tables.immutableCell(
					rowEntry.getKey(),
					columnEntry.getKey(),
					columnEntry.getValue());
		}
		
		public void remove() {
			columnIterator.remove();
			if (rowEntry.getValue().isEmpty()) {
				rowIterator.remove();
				rowEntry = null;
			}
		}

	}
	
	
	
	
	
	
	

	@Override
	public Map<C, V> row(R rowKey) {
		return new Row(rowKey);
	}
	
	class Row extends IteratorBasedAbstractMap<C, V> {
		final R rowKey;
		Map<C, V> backingRowMap;
		
		Row(R rowKey) {
			this.rowKey = rowKey;
		}
		
		final void updateBackingRowMapField() {
			if (backingRowMap == null
					|| (backingRowMap.isEmpty() && backingMap.containsKey(rowKey))) {
				backingRowMap = backingMap.get(rowKey);
			}
		}
		
		/** Called every time we remove an entry */
		void maintainEmptyInvariant() {
			updateBackingRowMapField();
			if (backingRowMap != null && backingRowMap.isEmpty()) {
				backingMap.remove(rowKey);
				backingRowMap = null;
			}
		}
		
		public boolean containsKey(Object key) {
			updateBackingRowMapField();
			return (key != null && backingRowMap != null)
					&& Maps.safeContainsKey(backingRowMap, key);
		}
		
		public V get(Object key) {
			return (key != null && backingRowMap != null)
					? Maps.safeGet(backingRowMap, key)
					: null;
		}
		
		public V put(C key, V value) {
			if (key == null || value == null) {
				return null;
			}
			if (backingRowMap != null && !backingRowMap.isEmpty()) {
				return backingRowMap.put(key, value);
			}
			return StandardTable.this.put(rowKey,  key,  value);
		}
		
		public V remove(Object key) {
			updateBackingRowMapField();
			if (backingRowMap == null) {
				return null;
			}
			V result = Maps.safeRemove(backingRowMap, key);
			maintainEmptyInvariant();
			return result;
		}
		
		public void clear() {
			updateBackingRowMapField();
			if (backingRowMap != null) {
				backingRowMap.clear();
			}
			maintainEmptyInvariant();
		}
		
		public int size() {
			updateBackingRowMapField();
			return (backingRowMap == null)
					? 0
					: backingRowMap.size();
		}

		@Override
		Iterator<Entry<C, V>> entryIterator() {
			updateBackingRowMapField();
			if (backingRowMap == null) {
				return Iterators.emptyModifiableIterator();
			}
			final Iterator<Entry<C, V>> iterator = backingRowMap.entrySet().iterator();
			return new Iterator<Entry<C, V>>() {
				public boolean hasNext() {
					return iterator.hasNext();
				}
				
				public Entry<C, V> next() {
					return wrapEntry(iterator.next());
				}
				
				public void remove() {
					iterator.remove();
					maintainEmptyInvariant();
				}
			};
		}
		
		Entry<C, V> wrapEntry(final Entry<C, V> entry) {
	      return new ForwardingMapEntry<C, V>() {
	          @Override
	          protected Entry<C, V> delegate() {
	            return entry;
	          }

	          @Override
	          public V setValue(V value) {
	            return super.setValue(checkNotNull(value));
	          }

	          @Override
	          public boolean equals(@CheckForNull Object object) {
	            // TODO(lowasser): identify why this affects GWT tests
	            return standardEquals(object);
	          }
	        };
		}
	}

	@Override
	public Map<R, V> column(C columnKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<R, Map<C, V>> rowMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<C, Map<R, V>> columnMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
