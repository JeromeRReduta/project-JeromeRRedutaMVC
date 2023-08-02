package data.stem_counting;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.collect.RowSortedTable;
import com.google.common.collect.TreeBasedTable;

import data.AbstractStringKeyTable;
import data.SearchEngineStringKeyTable;
import json.JsonMapWriter;
import json.JsonTableWriter;
import json.JsonWriter;

public class StemCounterTable
	extends AbstractStringKeyTable<Integer>
	implements StemCounter {
	
	private Map<String, Integer> stemCountsByFile;
	
	public StemCounterTable() {
		this.stemCountsByFile = new TreeMap<>();
	}

	@Override
	public SearchEngineStringKeyTable<Integer> snapshot() {
		StemCounterTable snapshot = new StemCounterTable();
		cellSet().forEach( cell -> {
			snapshot.add(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
		});
		return snapshot;
	}
	
	@Override
	public Integer put(String stem, String fileName, Integer count) {
		Integer originalValue = super.get(stem, fileName);
		super.put(stem,  fileName, count);
		stemCountsByFile.putIfAbsent(fileName, 0);
		stemCountsByFile.merge(fileName,  count,  (a, b) -> a + b);
		return originalValue;
	}

	@Override
	public void add(String stem, String fileName, int count) {
		put(stem, fileName, count);
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
	
	@Override
	public void writeToJson(int baseIndent, Writer writer) throws IOException {
		JsonMapWriter.writeMap(baseIndent, writer, stemCountsByFile);
	}

	@Override
	public String toString() {
		try {
			Writer writer = new StringWriter();
			JsonTableWriter<String, String, Integer> tableWriter = (bI, w, e) -> JsonWriter.writeIndented(bI, writer, e.toString());
			tableWriter.writeAllElements(0, writer, backingTable);
			writer.write("__________________________________\n");
			writer.write("Totals by filename: \n");
			JsonMapWriter.writeMap(0, writer, stemCountsByFile);
			return writer.toString();
		}
		catch (IOException e) {
			return null;
		}
	}
}
