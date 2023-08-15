package controllers.stem_indexing;

import java.util.TreeSet;

import com.google.common.collect.RowSortedTable;

import controllers.JsonWriteableController;

/**
 * A separate kind of inverted index controller, for inverted indices that can output snapshots of
 * their current state
 * @author JRRed
 *
 */
public interface InvertedIndexControllerWithSnapshot extends JsonWriteableController {
	
	/**
	 * Returns a snapshot of the stem / fileName / all stem locations in a given file (as a TreeSet)
	 * triplets
	 * @return A snapshot of the stem / fileName / all stem locations in a given file (as a TreeSet)
	 * triplets
	 */
	RowSortedTable<String, String, TreeSet<Integer>> snapshot();
}
