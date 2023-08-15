package models.stem_counting;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.collect.RowSortedTable;
import com.google.common.collect.TreeBasedTable;

import json.JsonMapWriter;
import json.JsonTableWriter;
import json.JsonWriter;
import models.AbstractStringKeyTable;

/**
 * Table based implementation of StemCounter.
 * @author JRRed
 *
 */
public class StemCounterTable extends AbstractStringKeyTable<Integer> implements StemCounter {
	
	/** This lets us track total stems based on each fileName */
	private Map<String, Integer> stemCountsByFile;
	
	public StemCounterTable() {
		this.stemCountsByFile = new TreeMap<>();
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
	public String toString() { // Note that we're not using toJsonString() b/c we want to output both the table and map data structures
		try {
			Writer writer = new StringWriter();
			JsonTableWriter<String, String, Integer> tableWriter
				= (bI, w, e) -> JsonWriter.writeIndented(bI, writer, e.toString());
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
