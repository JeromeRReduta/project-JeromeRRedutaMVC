package data_reading.stem_counting;

/**
 * Counts the number of stems an inverted index with snapshot has in each stem / fileName / TreeSet
 * of positions triplet
 * @author JRRed
 *
 */
public interface InvertedIndexSnapshotCounter {

	/** Counts the stems in an inverted index snapshot,
	 *  adding the results to a stem counter. The implementation,
	 *  inverted index snapshot, and stem counter are all the responsibilities
	 *  of the implementation.
	 */
	void countStems();


	/**
	 * Version of countStems with error-checking
	 */
	default void tryCountingStems() {
		try {
			countStems();
		}
		catch (Exception e) {
			System.err.println("Error in invertedindex snapshot counter - cancelling read...");
			e.printStackTrace();
		}
	}
}
