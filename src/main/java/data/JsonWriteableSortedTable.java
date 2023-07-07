package data;

import com.google.common.collect.RowSortedTable;

import json.JsonWriteable;

/**
 * An interface for a row sorted table that can be written in JSON format
 * @author JRRed
 *
 * @param <R> Row type
 * @param <C> column type
 * @param <V> value type
 */
public interface JsonWriteableSortedTable<R, C, V>
	extends RowSortedTable<R, C, V>, JsonWriteable {
}
