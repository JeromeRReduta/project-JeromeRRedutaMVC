package data_reading.search_result_indexing;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.RowSortedTable;

import data.search_result_indexing.SearchResultIndex;
import data.search_result_indexing.SearchResultIndex.SearchResult;
import data.search_result_indexing.SearchResultIndex.SearchResultFactory;
import data.stem_counting.StemCounter;
import data_reading.stem_indexing.TextStemmer;

public class SimpleStemCounterSearcher implements StemCounterSearcher {
	
	private final StemCounter counter;
	/*
	private RowSortedTable<String, String, Integer> stemCountTableSnapshot;
	
	private Map<String, Integer> totalStemsByFileNameSnapshot;
	*/
	
	private Path queries;
	
	private TextStemmer<String> stemmer;
	
	private SearchResultIndex index;
	
	private StemMatchingStrategy stemMatchingStrategy;
	
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

	@Override
	public void searchStemCounter() throws IOException {
		/*
		private RowSortedTable<String, String, Integer> stemCountTableSnapshot;
		
		private Map<String, Integer> totalStemsByFileNameSnapshot;
		*/
		
		RowSortedTable<String, String, Integer> tableSnapshot = counter.snapshotOfStemCountTable();
		Map<String, Integer> totalStemsSnapshot = counter.snapshotOfStemCountsByFile();
		
		try (BufferedReader reader = Files.newBufferedReader(queries)) {
			reader.lines()
				.map(this::safeUniqueStems)
				.filter(stems -> !stems.isEmpty())
				.forEach(queryStems -> search(queryStems, tableSnapshot, totalStemsSnapshot));
		}
	}
	
	private Collection<String> safeUniqueStems(String line) {
		try {
			return stemmer.uniqueStems(line);
		}
		catch (Exception e) {
			System.err.println("SHOULD NEVER RUN");
			throw new IllegalArgumentException("Shouldn't run");
		}
	}
	
	// TODO: add extra args
	private void search(
			Collection<String> queryStems,
			RowSortedTable<String, String, Integer> tableSnapshot,
			Map<String, Integer> totalStemsSnapshot) {
		String queryAsLine = queryStems.toString().replaceAll(BRACKET_AND_COMMA_REGEX, "");
		List<SearchResult> results = tableSnapshot.columnMap().entrySet().stream()
			.map(entry -> mapSnapshotEntryToResult(entry, queryStems, totalStemsSnapshot))
			.filter(result -> result != null)
			.collect(Collectors.toList());
		index.addAll(queryAsLine, results);
	}
	
	private SearchResult mapSnapshotEntryToResult(Map.Entry<String, Map<String, Integer>> snapshotEntry, Collection<String> queryStems,
			Map<String, Integer> totalStemsSnapshot) {
		String fileName = snapshotEntry.getKey();
		Number matches = snapshotEntry.getValue().entrySet().stream()
				.mapToInt(entry -> numberOfMatches(entry, queryStems))
				.sum();
		Number stemCount = totalStemsSnapshot.getOrDefault(fileName, 0);
		return SearchResultFactory.create(fileName, matches, stemCount);
	}
	
	int numberOfMatches(
			Map.Entry<String, Integer> entry,
			Collection<String> queryStems) {
		Integer stemCount = entry.getValue();
		if (stemCount == null) {
			return 0;
		}
		int numberOfMatchingQueryStems = (int)queryStems.stream()
			.filter(stem -> stemMatchingStrategy.stemsMatch(entry.getKey(), stem))
			.count();
		return stemCount * numberOfMatchingQueryStems;
	}
}
