package table_value_transforming;

import data.AbstractStringKeyTable;


public interface StringKeyTableValueTransformer<V1, V2> {
	
	void transform();
	
	/**
	 * Adds cells with transformed values from one table to another
	 * @param <R> row type
	 * @param <C> column type
	 * @param source source table
	 * @param destination destination table
	 */
	default <R, C> void transform(AbstractStringKeyTable<V1> source, AbstractStringKeyTable<V2> destination) {
		source.cellSet().stream()
			.forEach(cell -> destination.put(
					cell.getRowKey(),
					cell.getColumnKey(),
					transformValue(cell.getValue())));
	}
	/**
	 * Determines how the value from one cell is transformed
	 * @param <V1> value type of source table
	 * @param <V2> value type of destination table
	 * @param value input value
	 * @return transformed version of input value
	 */
	V2 transformValue(V1 value);
}
