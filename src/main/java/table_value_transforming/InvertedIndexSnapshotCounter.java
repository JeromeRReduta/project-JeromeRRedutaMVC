package table_value_transforming;

public interface InvertedIndexSnapshotCounter {

	/** Counts the stems in an inverted index snapshot,
	 *  adding the results to a stem counter. The implementation,
	 *  inverted index snapshot, and stem counter are all the responsibilities
	 *  of the implementation.
	 */
	void countStems();


}
