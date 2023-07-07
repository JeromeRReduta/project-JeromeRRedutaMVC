package data.stem_counting;

import java.util.Map;

import java.util.TreeMap;

import com.google.common.collect.RowSortedTable;
import com.google.common.collect.TreeBasedTable;

import data.AbstractStemFileNameValueTable;
import data.StemFileNameValueTable;
import json.JsonWriter;

public class StemCounterTable
	extends AbstractStemFileNameValueTable<Integer>
	implements StemCounter {
	
	private Map<String, Integer> stemCountsByFile;
	
	public StemCounterTable() {
		super((bI, w, e) -> JsonWriter.writeIndented(bI, w, e.toString()));
		this.stemCountsByFile = new TreeMap<>();
	}

	@Override
	public StemFileNameValueTable<Integer> snapshot() {
		StemCounterTable clone = new StemCounterTable();
		cellSet().forEach( cell -> {
			clone.add(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
		});
		return clone;
	}

	@Override
	public void add(String stem, String fileName, int count) {
		super.put(stem, fileName, count);
		stemCountsByFile.putIfAbsent(fileName, 0);
		stemCountsByFile.merge(fileName, count, (a, b) -> a + b);
	}

	@Override
	public RowSortedTable<String, String, Integer> snapshotOfStemCountTable() {
		return TreeBasedTable.create(
				(TreeBasedTable<String, String, Integer>)super.backingTable); // This is safe b/c under the hood the backing table is always a TreeBasedTable
	}

	@Override
	public Map<String, Integer> snapshotOfStemCountsByFile() {
		return new TreeMap<>(stemCountsByFile);
	}
}
