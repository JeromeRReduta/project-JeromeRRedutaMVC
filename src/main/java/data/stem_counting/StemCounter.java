package data.stem_counting;

import java.util.Map;

import com.google.common.collect.RowSortedTable;

/**
 * A nested data structure. Similar to the InvertedIndex but instead
 * it tracks how many times a given stem shows up in a given file
 * @author JRRed
 *
 */
public interface StemCounter {
	
	/**
	 * Adds a stem / fileName / count triplet to the stem counter
	 * @param stem stem
	 * @param fileName fileName
	 * @param count count
	 */
	void add(String stem, String fileName, int count);
	
	/**
	 * Returns a snapshot of the stem counter, as a table of
	 * stem / fileName / stem count triplets
	 * @return a stem counter table view
	 */
	RowSortedTable<String, String, Integer> snapshotOfStemCountTable();
	
	/**
	 * Returns a snapshot of the stem counter, as a map of
	 * fileName / total stem count pairs
	 * @return a stem counter map view
	 */
	Map<String, Integer> snapshotOfStemCountsByFile();
}
