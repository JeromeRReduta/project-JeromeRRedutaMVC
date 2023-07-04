package data;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import json.JsonTableWriter;

/**
 * Abstract implementation of a JsonWriteableSortedTable. This implementation maps
 * stem / fileName / value triplets.
 * @author JRRed
 *
 * @param <V> Value type
 */
public abstract class AbstractStemAndFileNameTable<V> implements JsonWriteableSortedTable<String, String, V> {

	private TreeBasedTable<String, String, V> backingTable;
	private JsonTableWriter<String, String, V> tableWriter;
	
	public AbstractStemAndFileNameTable(JsonTableWriter<String, String, V> tableWriter) {
		this(TreeBasedTable.create(), tableWriter);
	}
	
	public AbstractStemAndFileNameTable(
			TreeBasedTable<String, String, V> backingTable, JsonTableWriter<String, String, V> tableWriter) {
		this.backingTable = backingTable;
		this.tableWriter = tableWriter;
	}

	@Override
	public SortedSet<String> rowKeySet() {
		return backingTable.rowKeySet();
	}

	@Override
	public SortedMap<String, Map<String, V>> rowMap() {
		return backingTable.rowMap();
	}

	@Override
	public boolean contains(Object rowKey, Object columnKey) {
		return backingTable.contains(rowKey, columnKey);
	}

	@Override
	public boolean containsRow(Object rowKey) {
		return backingTable.containsRow(rowKey);
	}

	@Override
	public boolean containsColumn(Object columnKey) {
		return backingTable.containsColumn(columnKey);
	}

	@Override
	public boolean containsValue(Object value) {
		return backingTable.containsValue(value);
	}

	@Override
	public V get(Object rowKey, Object columnKey) {
		return backingTable.get(rowKey, columnKey);
	}

	@Override
	public boolean isEmpty() {
		return backingTable.isEmpty();
	}

	@Override
	public int size() {
		return backingTable.size();
	}

	@Override
	public void clear() {
		backingTable.clear();
	}
	
	@Override
	public V put(String rowKey, String columnKey, V value) {
		return backingTable.put(rowKey, columnKey, value);
	}

	@Override
	public void putAll(Table<? extends String, ? extends String, ? extends V> table) {
		backingTable.putAll(table);
	}

	@Override
	public V remove(Object rowKey, Object columnKey) {
		return backingTable.remove(rowKey, columnKey);
	}

	@Override
	public Map<String, V> row(String rowKey) {
		return backingTable.row(rowKey);
	}

	@Override
	public Map<String, V> column(String columnKey) {
		return backingTable.column(columnKey);
	}

	@Override
	public Set<Cell<String, String, V>> cellSet() {
		return backingTable.cellSet();
	}

	@Override
	public Set<String> columnKeySet() {
		return backingTable.columnKeySet();
	}

	@Override
	public Collection<V> values() {
		return backingTable.values();
	}

	@Override
	public Map<String, Map<String, V>> columnMap() {
		return backingTable.columnMap();
	}

	@Override
	public void writeToJson(int baseIndent, Writer writer) throws IOException {
		tableWriter.writeAllElements(baseIndent, writer, backingTable);
	}
	
	@Override
	public String toString() {
		return toJsonString();
	}
}
