package data;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import json.JsonCollectionWriter;
import json.JsonMapWriter;
import json.JsonTableWriter;
import json.JsonWriteable;
import json.JsonWriter;

public class InvertedIndexTable implements InvertedIndex {
	
	private Table<String, String, TreeSet<Integer>> backingTable;
	
	public InvertedIndexTable() {
		backingTable = TreeBasedTable.create();
	}

	@Override
	public void writeToJson(int baseIndent, Writer writer) throws IOException {
		JsonTableWriter<String, String, TreeSet<Integer>> tableWriter = JsonCollectionWriter::writeCollection;
		tableWriter.writeAllElements(baseIndent, writer, backingTable);
	}

	@Override
	public void add(String stem, String fileName, int position) {
		if (backingTable.get(stem, fileName) == null) {
			backingTable.put(stem, fileName, new TreeSet<>());
		}
		backingTable.get(stem, fileName).add(position);
	}

	@Override
	public Map<String, ? extends Collection<Integer>> get(String stem) {
		return backingTable.row(stem);
	}
	
	public String toString() {
		try {
			return toJsonString();
		} catch (IOException e) {
			return null;
		}
	}
}
