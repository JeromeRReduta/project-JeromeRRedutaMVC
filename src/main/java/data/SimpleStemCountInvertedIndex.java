package data;

import java.util.Map;
import java.util.TreeMap;

import json.JsonWriter;


public class SimpleStemCountInvertedIndex
	extends AbstractStemAndFileNameTable<Integer>
	implements StemCountInvertedIndex {
	
	private Map<String, Integer> stemCountsByFile;

	public SimpleStemCountInvertedIndex() {
		super((bI, w, e) -> JsonWriter.writeIndented(bI, w, e.toString()));
		this.stemCountsByFile = new TreeMap<>();
	}

	@Override
	public JsonWriteableSortedTable<String, String, Integer> snapshot() {
		JsonWriteableSortedTable<String, String, Integer> indexClone = new SimpleStemCountInvertedIndex();
		super.cellSet().forEach(cell -> {
			indexClone.put(
					cell.getRowKey(),
					cell.getColumnKey(),
					cell.getValue());
		});
		return indexClone;
	}

	@Override
	public void add(String stem, String fileName, int stemCount) {
		super.put(stem, fileName, stemCount);
		stemCountsByFile.putIfAbsent(fileName, 0);
		stemCountsByFile.merge(fileName, stemCount, (a, b) -> a + b);
	}

	@Override
	public Map<String, Integer> getStemCountsByFile() {
		return new TreeMap<>(stemCountsByFile);
	}
}
