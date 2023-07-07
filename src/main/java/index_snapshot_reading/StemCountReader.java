package index_snapshot_reading;

import controllers.InvertedIndexController;
import data.InvertedIndex;

/**
 * Reads data (in the form of InvertedIndex data processed into search data) into
 * a MatchDataInvertedIndex. The MatchDataInvertedIndex and how the data gets processed is up to the
 * implementation.
 * @author JRRed
 *
 */
public interface StemCountReader {
	
	/**
	 * Reads all data from an InvertedIndex snapshot, processes it, and stores results in a
	 * MatchDataInvertedIndex
	 * @param snapshot snapshot of an InvertedIndex
	 */
	default void tryReadingIntoMatchDataInvertedIndex(InvertedIndex snapshot) {
		try {
			readIntoMatchDataInvertedIndex(snapshot);
		}
		catch (NullPointerException e) {
			System.err.println("No index found; cancelling read");
		}
	}
	
	/**
	 * See tryReadingIntoMatchDataInvertedIndex.
	 * @param snapshot snapshot of an InvertedIndex
	 */
	void readIntoMatchDataInvertedIndex(InvertedIndex snapshot);
}
