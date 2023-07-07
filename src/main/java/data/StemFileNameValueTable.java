package data;

import com.google.common.collect.RowSortedTable;

import json.JsonWriteable;

public interface StemFileNameValueTable<V>
	extends RowSortedTable<String, String, V>, JsonWriteable {
	
	StemFileNameValueTable<V> snapshot();
}
