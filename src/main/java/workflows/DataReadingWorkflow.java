package workflows;

import stem_reading.StemReader;

/**
 * A workflow that reads data into an Inverted Index
 * @author JRRed
 *
 */
public interface DataReadingWorkflow {
	
	/**
	 * If requested, reads data into an inverted index using a stem reader
	 * @param isRequested
	 * @param reader
	 */
	static void runIfRequested(boolean isRequested, StemReader<?> reader) {
		if (isRequested) {
			reader.tryReadingIntoInvertedIndex();
		}
	}
}
