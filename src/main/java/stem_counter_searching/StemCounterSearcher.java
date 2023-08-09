package stem_counter_searching;

import java.io.IOException;

/**
 * Searches a stem counter, adding its search results to a SearchResultIndex. How the search happens, the stem
 * counter, and the searchResultIndex are the responsibilities of the implementation
 * @author JRRed
 *
 */
public interface StemCounterSearcher {
	
	/**
	 * Searches the stem counter and adds search results to a SearchResultIndex, with error
	 * handling.
	 */
	default void trySearchingStemCounter() {
		try {
			searchStemCounter();
		}
		catch (IOException e) {
			System.err.println("Error in StemCounterSearcher; cancelling search");
			e.printStackTrace();
		}
		catch (Exception e) {
			System.err.println("Error in StemCounterSearcher; cancelling search");
			e.printStackTrace();
		}
	}
	
	/**
	 * Searches the stem counter and adds search results to a SearchResultIndex.
	 * @throws IOException in case of IOError
	 */
	void searchStemCounter() throws IOException;
	
	public interface StemMatchingStrategy {
		
		static final StemMatchingStrategy EXACT_MATCH =
				(dataStem, queryStem) -> dataStem.equals(queryStem);
				
		static final StemMatchingStrategy PARTIAL_MATCH =
				(dataStem, queryStem) -> dataStem.startsWith(queryStem);
				
		static StemMatchingStrategy getDefault(boolean shouldDoExactSearch) {
			return shouldDoExactSearch
					? EXACT_MATCH
					: PARTIAL_MATCH;
		}
		
		boolean stemsMatch(String dataStem, String queryStem);
	}
}
