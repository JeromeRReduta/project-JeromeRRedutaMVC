package models;

import com.google.common.collect.RowSortedTable;

import json.JsonWriteable;

/**
 * A common interface for a table w/ string keys, used in this project
 * @author JRRed
 *
 * @param <V> value type
 */
public interface SearchEngineStringKeyTable<V> extends RowSortedTable<String, String, V>, JsonWriteable {}
