package data;

import com.google.common.collect.RowSortedTable;

import json.JsonWriteable;

public interface SearchEngineStringKeyTable<V>
	extends RowSortedTable<String, String, V>, JsonWriteable {
}
