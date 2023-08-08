package workflows;

import controllers.JsonWriteableController;
import controllers.SearchResultIndexController;
import controllers.StemCounterController;
import stem_counter_searching.StemCounterSearcher;
import stem_reading.StemReader;
import table_value_transforming.InvertedIndexSnapshotCounter;

/**
 * A workflow that reads data into an Inverted Index
 * @author JRRed
 *
 */
public interface Workflows {
	
	
	static interface ReadIntoInvertedIndex {
		/**
		 * If requested, reads data into an inverted index using a stem reader
		 * @param isRequested should we read?
		 * @param reader StemReader
		 */
		static void runIfRequested(boolean isRequested, StemReader<?> reader) {
			if (isRequested) {
				reader.tryReadingIntoInvertedIndex();
			}
		}
	}
	
	static interface DisplayIndex {
		
		/**
		 * If requested, attempts to write a JSONWriteable to a file.
		 * @param isRequested should we write?
		 * @param controller JsonWriteableController. Rare case where we want any and all JsonWriteableControllers.
		 */
		static void runIfRequested(boolean isRequested, JsonWriteableController controller) {
			if (isRequested) {
				controller.tryDisplaying();
			}
		}
	}
	
	static interface ReadIntoStemCounter {
		
		static void runIfRequested(boolean isRequested, InvertedIndexSnapshotCounter snapshotCounter) {
			if (isRequested) {
				snapshotCounter.countStems(); // TODO - make this a tryCountingStems()
			}
		}
	}
	
	static interface DisplayStemCounter {
		
		static void runIfRequested(boolean isRequested, StemCounterController controller) {
			if (isRequested) {
				controller.tryDisplaying();
			}
		}
	}
	
	static interface ReadIntoSearchResultIndex {
		
		static void runIfRequested(boolean isRequested, StemCounterSearcher searcher) {
			if (isRequested) {
				searcher.trySearchingStemCounter();
			}
		}
	}
	
	static interface DisplaySearchResultIndex {
		
		static void runIfRequested(boolean isRequested, SearchResultIndexController controller) {
			if (isRequested) {
				controller.tryDisplaying();
			}
		}
	}
}
