package stem_counter_searching;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
	
	private static final String BRACKET_AND_COMMA_REGEX = "[\\[\\],]";
	
	public SimpleStemCounterSearcher(
			StemCounter stemCounter,
			Path queries,
			TextStemmer<String> stemmer,
			SearchResultIndex index) {
		this.stemCountTableSnapshot = stemCounter.snapshotOfStemCountTable();
		this.totalStemsByFileNameSnapshot = stemCounter.snapshotOfStemCountsByFile();
		this.queries = queries;
		this.stemmer = stemmer;
		this.index = index;
	}

	@Override
	public void searchStemCounter() throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(queries)) {
			reader.lines()
				.map(this::safeUniqueStems)
				.filter(stems -> !stems.isEmpty())
				.forEach(this::exactSearch);
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
	
	private void exactSearch(Collection<String> queryStems) {
		String queryAsLine = queryStems.toString().replaceAll(BRACKET_AND_COMMA_REGEX, "");
		List<SearchResult> results = stemCountTableSnapshot.columnMap().entrySet().stream()
			.map(entry -> mapSnapshotEntryToResult(entry, queryStems))
			.filter(result -> result != null)
			.collect(Collectors.toList());
		index.addAll(queryAsLine, results);
	}
	
	private SearchResult mapSnapshotEntryToResult(Map.Entry<String, Map<String, Integer>> snapshotEntry, Collection<String> queryStems) {
		String fileName = snapshotEntry.getKey();
		Number matches = getMatches(snapshotEntry.getValue(), queryStems);
		Number stemCount = totalStemsByFileNameSnapshot.getOrDefault(fileName, 0);
		return SearchResultFactory.create(fileName, matches, stemCount);
	}
	
	private int getMatches(Map<String, Integer> stemToCountPair, Collection<String> queryStems) {
		return queryStems.stream()
				.map(stem -> stemToCountPair.get(stem))
				.filter(count -> count != null)
				.reduce(0, Integer::sum);
	}
}
