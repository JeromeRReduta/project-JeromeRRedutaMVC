package data.stem_indexing;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;

import data.AbstractStringKeyTable;
import data.SearchEngineStringKeyTable;
import json.JsonCollectionWriter;
import json.JsonTableWriter;

public class InvertedIndexTable 
	extends AbstractStringKeyTable<TreeSet<Integer>>
	implements InvertedIndexWithSnapshot {

	public InvertedIndexTable() {
	}

	@Override
	public SearchEngineStringKeyTable<TreeSet<Integer>> snapshot() {
		InvertedIndexTable clone = new InvertedIndexTable();
		cellSet().forEach(cell -> {
			clone.put(
					cell.getRowKey(),
					cell.getColumnKey(),
					new TreeSet<>(cell.getValue())); // we make a new treeset to preserve data integrity
		});
		return clone;
	}

	@Override
	public void add(String stem, String fileName, int position) {
		if (super.get(stem, fileName) == null) {
			super.put(stem, fileName, new TreeSet<>());
		}
		super.get(stem, fileName).add(position);
	}

	@Override
	public Map<String, ? extends Collection<Integer>> get(String stem) {
		return super.row(stem);
	}
	
	@Override
	public String toString() {
		return toJsonString();
	}

	@Override
	public void writeToJson(int baseIndent, Writer writer) throws IOException {
		JsonTableWriter<String, String, TreeSet<Integer>> tableWriter
			= JsonCollectionWriter::writeCollection;
		tableWriter.writeAllElements(baseIndent, writer, backingTable);
	}
}
