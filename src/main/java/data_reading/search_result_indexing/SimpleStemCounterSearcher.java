package data_reading.search_result_indexing;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.RowSortedTable;

import data_reading.stem_indexing.TextStemmer;
import models.search_result_indexing.SearchResultIndex;
import models.search_result_indexing.SearchResultIndex.SearchResult;
import models.search_result_indexing.SearchResultIndex.SearchResultFactory;
import models.stem_counting.StemCounter;

/**
 * Implementation of a StemCounterSearcher
 * @author JRRed
 *
 */
public class SimpleStemCounterSearcher implements StemCounterSearcher {
	
	private final StemCounter counter;
	
	private final Path queries;
	
	private final TextStemmer<String> stemmer;
	
	private final SearchResultIndex index;
	
	private final StemMatchingStrategy stemMatchingStrategy;
	
	private RowSortedTable<String, String, Integer> tableSnapshot;
	
	private Map<String, Integer> totalStemsSnapshot;
	
	/** Finds any occurrence of "[", "]", or "," */
	private static final String BRACKET_AND_COMMA_REGEX = "[\\[\\],]";
	
	public SimpleStemCounterSearcher(
			StemCounter counter,
			Path queries,
			TextStemmer<String> stemmer,
			SearchResultIndex index,
			StemMatchingStrategy stemMatchingStrategy) {
		this.counter = counter;
		this.queries = queries;
		this.stemmer = stemmer;
		this.index = index;
		this.stemMatchingStrategy = stemMatchingStrategy;
	}
	
	/**
	 * Takes a snapshot of the stem counter's data structs and saves it to this instance
	 */
	private void updateStemCounterSnapshots() {
		tableSnapshot = counter.snapshotOfStemCountTable();
		totalStemsSnapshot = counter.snapshotOfStemCountsByFile();
	}

	@Override
	public void searchStemCounter() throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(queries)) {
			updateStemCounterSnapshots();
			if (tableSnapshot == null || totalStemsSnapshot == null) {
				System.err.println("shouldn't really run");
				return;
			}
			reader.lines()
				.map(this::safeUniqueStems)
				.filter(stems -> !stems.isEmpty())
				.forEach(this::search);
		}
	}
	
	/**
	 * Utility method for getting unique stems from a text line stemmer, since there should never,
	 * ever be an IOException when stemming just a text line
	 * @param line
	 * @return a collection of unique stems, processed from a text line
	 */
	private Collection<String> safeUniqueStems(String line) {
		try {
			return stemmer.uniqueStems(line);
		}
		catch (Exception e) {
			System.err.println("SHOULD NEVER RUN");
			throw new IllegalArgumentException("Shouldn't run");
		}
	}
	
	/**
	 * Searches the stem counter and adds the search results into the search result index
	 * @param queryStems collection of query stems
	 * @param tableSnapshot snapshot of the StemCounterTable
	 * @param totalStemsSnapshot snapshot of the stem counts by file name
	 */
	private void search(Collection<String> queryStems) {
		String queryAsLine = queryStems.toString().replaceAll(BRACKET_AND_COMMA_REGEX, "");
		Collection<SearchResult> results = tableSnapshot.columnMap().entrySet().stream()
			.map(entry -> mapSnapshotEntryToResult(entry, queryStems))
			.filter(result -> result != null)
			.collect(Collectors.toList());
		index.addAll(queryAsLine, results);
	}
	
	/**
	 * Converts a table snapshot entry into a search result, using a collection of query stems
	 * @param tableSnapshotEntry table snapshot entry
	 * @param queryStems query stems
	 * @return the Search Result from searching the stem counter's snapshots with the given query
	 * stems
	 */
	private SearchResult mapSnapshotEntryToResult(
			Map.Entry<String, Map<String, Integer>> tableSnapshotEntry, Collection<String> queryStems) {
		String fileName = tableSnapshotEntry.getKey();
		Number matches = tableSnapshotEntry.getValue().entrySet().stream()
				.mapToInt(entry -> numberOfMatches(entry, queryStems))
				.sum();
		Number stemCount = totalStemsSnapshot.getOrDefault(fileName, 0);
		return SearchResultFactory.create(fileName, matches, stemCount);
	}
	
	/**
	 * Gets the number of matches that a given snapshot entry has for a given collection of query
	 * stems. Here, the number of matches is defined as the stem count in the entry times the number
	 * of matching stems in query stems, based on the given stem matching strategy.
	 * 
	 * For example, if our strategy is to look for an exact match (e.g. (a, b) -> (a.equals(b))),
	 * and our entry is "bubba"/4 (as in there are 4 instances of the stem "bubba"), and our query stems 
	 * is ["bubba"], then we would have entry.getValue() * numberOfMatchingQueryStems = 4 * 1 = 4 matches
	 * 
	 * If our strategy is to look for a partial match (e.g. (a, b) -> a.startsWith(b), and our entry is
	 * "ab"/4, and our query stems is ["ab", "aba", "baz"], then we would have
	 * entry.getValue() * numberOfMatchingQueryStems = 4 * 2 = 8 matches
	 * @param entry tableSnapshot entry
	 * @param queryStems query stems
	 * @return the number of matches that a given snapshot entry has for the query stems
	 */
	private int numberOfMatches(Map.Entry<String, Integer> entry, Collection<String> queryStems) {
		Integer stemCount = Optional.ofNullable(entry.getValue()).orElse(0);
		int numberOfMatchingQueryStems = (int)queryStems.stream()
			.filter(stem -> stemMatchingStrategy.stemsMatch(entry.getKey(), stem))
			.count();
		return stemCount * numberOfMatchingQueryStems;
	}
}
