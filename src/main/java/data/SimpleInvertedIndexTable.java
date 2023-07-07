package data;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;

import json.JsonCollectionWriter;

/**
 * Implementation of both an InvetedIndex and an AbstractStemAndFileNameTable
 * @author JRRed
 *
 */
public class SimpleInvertedIndexTable
	extends AbstractStemAndFileNameTable<TreeSet<Integer>>
	implements InvertedIndexWithSnapshot {
	
	public SimpleInvertedIndexTable() {
		/* B/c the value of this table is a TreeSet, the tableWriter used here writes a collection */
		super(JsonCollectionWriter::writeCollection);
	}

	@Override
	public void add(String stem, String fileName, int position) {
		if (super.get(stem, fileName) == null) {
			super.put(stem, fileName, new TreeSet<Integer>());
		}
		super.get(stem, fileName).add(position);
	}

	/** In order to maintain data integrity, we clone the treeset value */
	@Override
	public InvertedIndexWithSnapshot snapshot() {
		SimpleInvertedIndexTable indexClone = new SimpleInvertedIndexTable();
		super.cellSet().forEach(cell -> {
			indexClone.put(
					cell.getRowKey(),
					cell.getColumnKey(),
					new TreeSet<>(cell.getValue()));
		});
		return indexClone;
	}
}
