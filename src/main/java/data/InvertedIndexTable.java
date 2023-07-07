package data;

import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;

import json.JsonCollectionWriter;

public class InvertedIndexTable 
	extends AbstractStemFileNameValueTable<TreeSet<Integer>>
	implements InvertedIndex {

	public InvertedIndexTable() {
		super(JsonCollectionWriter::writeCollection);
	}

	@Override
	public StemFileNameValueTable<TreeSet<Integer>> snapshot() {
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
}
