package simple_guava;

import java.util.Objects;

import simple_guava.Table.Cell;

/**
 * Static utility methods for a Table. Based off Guava's Tables class but heavily pared down.
 * @author JRRed
 *
 */
public final class Tables {
	
	private Tables() {}
	
	/**
	 * Checks that two tables are equal, defined as whether the two table's
	 * cellsets are equal
	 * @param table table
	 * @param object some object
	 * @return whether the two tables are equal. If the object isn't a table, returns false
	 */
	static boolean equalsImpl(Table<?, ?, ?> table, Object object) {
		if (object == table) {
			return true;
		}
		else if (object instanceof Table) {
			Table<?, ?, ?> otherTable = (Table<?, ?, ?>)object;
			return table.cellSet().equals(
					otherTable.cellSet());
		}
		else {
			return false;
		}
	}
	
	abstract static class AbstractCell
		<R extends Object, C extends Object, V extends Object>
		implements Cell<R, C, V> {
		
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			
			if (!(obj instanceof Cell)) {
				return false;
			}
			Cell<?, ?, ?> other = (Cell<?, ?, ?>) obj;
			return Objects.equals(getRowKey(), other.getRowKey())
					&& Objects.equals(getColumnKey(), other.getColumnKey())
					&& Objects.equals(getValue(), other.getValue());
					
		}
		
		public int hashCode() {
			return Objects.hash(
					getRowKey(),
					getColumnKey(),
					getValue());
		}
		
		@Override
		public String toString() {
			return "(" + getRowKey() + "," + getColumnKey() + ")=" + getValue();
		}
	}

	public static <R, C, V> Cell<R, C, V> immutableCell(R rowKey, C columnKey, V value) {
		return new ImmutableCell<>(rowKey, columnKey, value);
	}
	
	static final class ImmutableCell
		<R extends Object, C extends Object, V extends Object>
		extends AbstractCell<R, C, V> {
		
		private final R rowKey;
		private final C columnKey;
		private final V value;
		
		ImmutableCell(R rowKey, C columnKey, V value) {
			this.rowKey = rowKey;
			this.columnKey = columnKey;
			this.value = value;
		}

		@Override
		public R getRowKey() {
			return rowKey;
		}

		@Override
		public C getColumnKey() {
			return columnKey;
		}

		@Override
		public V getValue() {
			return value;
		}
		
	}
}
