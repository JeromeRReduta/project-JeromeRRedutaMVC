package simple_guava;

/**
 * Static utility methods for a Table. Based off Guava's Tables class but heavily pared down.
 * @author JRRed
 *
 */
public final class Tables {
	
	private Tables() {}
	
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
}
