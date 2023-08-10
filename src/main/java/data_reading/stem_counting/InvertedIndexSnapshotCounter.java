package data_reading.stem_counting;

public interface InvertedIndexSnapshotCounter {

	/** Counts the stems in an inverted index snapshot,
	 *  adding the results to a stem counter. The implementation,
	 *  inverted index snapshot, and stem counter are all the responsibilities
	 *  of the implementation.
	 */
	void countStems();


}
