package models.stem_indexing;

import java.util.TreeSet;

import com.google.common.collect.RowSortedTable;

/**
 * An InvertedIndex that can give a snapshot of its internal data
 * @author JRRed
 *
 */
public interface InvertedIndexWithSnapshot extends InvertedIndex {
	
	/**
	 * Returns a snapshot of the index's internal data
	 * @return A snapshot of the index's internal data
	 */
	RowSortedTable<String, String, TreeSet<Integer>> snapshot();
}
