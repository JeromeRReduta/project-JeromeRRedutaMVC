package workflows;

import controllers.JsonWriteableController;
import controllers.stem_indexing.InvertedIndexController;
import data_reading.search_result_indexing.StemCounterSearcher;
import data_reading.stem_counting.InvertedIndexSnapshotCounter;
import data_reading.stem_indexing.StemReader;

/**
 * Collection of workflows to make app logic more modular
 * @author JRRed
 */
public interface Workflows<E> {
	
	/**
	 * Runs an object's operation if requested
	 * @param isRequested if the operation is requested
	 * @param object some object
	 */
	void runIfRequested(boolean isRequested, E object);
	
	static Workflows<StemReader<?>> ReadIntoInvertedIndex = (isRequested, stemReader) -> {
		if (isRequested) {
			stemReader.tryReadingIntoInvertedIndex();
		}
	};
	
	static Workflows<InvertedIndexController> DisplayInvertedIndex = (isRequested, controller) -> {
		if (isRequested) {
			controller.tryDisplaying();
		}
	};
	
	static Workflows<JsonWriteableController> Display = (isRequested, controller) -> {
		if (isRequested) {
			controller.tryDisplaying();
		}
	};
	
	static Workflows<InvertedIndexSnapshotCounter> ReadIntoStemCounter = (isRequested, counter) -> {
		if (isRequested) {
			counter.tryCountingStems();
		}
	};
	
	static Workflows<StemCounterSearcher> ReadIntoSearchResultIndex = (isRequested, searcher) -> {
		if (isRequested) {
			searcher.trySearchingStemCounter();
		}
	};
}
