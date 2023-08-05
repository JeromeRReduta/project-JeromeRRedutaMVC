package stem_counter_searching;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.RowSortedTable;

import data.search_results.SearchResultIndex;
import data.search_results.SearchResultIndex.SearchResult;
import data.search_results.SearchResultIndex.SearchResultFactory;
import data.stem_counting.StemCounter;
import stem_reading.text_stemming.TextStemmer;

public class SimpleStemCounterSearcher implements StemCounterSearcher {
	
	private RowSortedTable<String, String, Integer> stemCountTableSnapshot;
	
	private Map<String, Integer> totalStemsByFileNameSnapshot;
	
	private Path queries;
	
	private TextStemmer<String> stemmer;
	
	private SearchResultIndex index;
	
	private StemMatchingStrategy stemMatchingStrategy;
	
	private static final String BRACKET_AND_COMMA_REGEX = "[\\[\\],]";
	
	public SimpleStemCounterSearcher(
			StemCounter stemCounter,
			Path queries,
			TextStemmer<String> stemmer,
			SearchResultIndex index,
			StemMatchingStrategy stemMatchingStrategy) {
		this.stemCountTableSnapshot = stemCounter.snapshotOfStemCountTable();
		this.totalStemsByFileNameSnapshot = stemCounter.snapshotOfStemCountsByFile();
		this.queries = queries;
		this.stemmer = stemmer;
		this.index = index;
		this.stemMatchingStrategy = stemMatchingStrategy;
	}

	@Override
	public void searchStemCounter() throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(queries)) {
			reader.lines()
				.map(this::safeUniqueStems)
				.filter(stems -> !stems.isEmpty())
				.forEach(this::search);
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
	
	private void search(Collection<String> queryStems) {
		String queryAsLine = queryStems.toString().replaceAll(BRACKET_AND_COMMA_REGEX, "");
		List<SearchResult> results = stemCountTableSnapshot.columnMap().entrySet().stream()
			.map(entry -> mapSnapshotEntryToResult(entry, queryStems))
			.filter(result -> result != null)
			.collect(Collectors.toList());
		index.addAll(queryAsLine, results);
	}
	
	private SearchResult mapSnapshotEntryToResult(Map.Entry<String, Map<String, Integer>> snapshotEntry, Collection<String> queryStems) {
		String fileName = snapshotEntry.getKey();
		Number matches = snapshotEntry.getValue().entrySet().stream()
				.mapToInt(entry -> numberOfMatches(entry, queryStems))
				.sum();
		Number stemCount = totalStemsByFileNameSnapshot.getOrDefault(fileName, 0);
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
