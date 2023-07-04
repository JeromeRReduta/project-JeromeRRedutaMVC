package data;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;

import json.JsonCollectionWriter;


/* TODO:
 * (x) 1. Formatting + documentation for:
 * 	InvIndexTable
 *	JsonTableWriter
 *
 *	(x) 1.5. Deprecate all other forms of InvertedIndex not based on tables
 *
 *	(x) 2. Organize import
 *
 *	(x) 3. See if you can rename this to Project 1.5 - Project 1 w/ Tables
 *
 *	(x) 4. Make CRC cards
 *
 *	5. Add CRC cards to README
 */

/**
 * Implementation of both an InvetedIndex and an AbstractStemAndFileNameTable
 * @author JRRed
 *
 */
public class SimpleInvertedIndexTable
	extends AbstractStemAndFileNameTable<TreeSet<Integer>>
	implements InvertedIndex {
	
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

	@Override
	public Map<String, ? extends Collection<Integer>> get(String stem) {
		return super.row(stem);
	}
	
	/** In order to maintain data integrity, we clone the treeset value */
	@Override
	public JsonWriteableSortedTable<String, String, TreeSet<Integer>> snapshot() {
		JsonWriteableSortedTable<String, String, TreeSet<Integer>> indexClone = new SimpleInvertedIndexTable();
		super.cellSet().forEach(cell -> {
			indexClone.put(
					cell.getRowKey(),
					cell.getColumnKey(),
					new TreeSet<>(cell.getValue()));
		});
		return indexClone;
	}
}
