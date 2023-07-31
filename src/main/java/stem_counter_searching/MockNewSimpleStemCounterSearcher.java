package stem_counter_searching;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.RowSortedTable;

import data.search_results.MockSearchResultIndex;
import data.stem_counting.StemCounter;
import stem_reading.text_stemming.TextStemmer;

public class MockNewSimpleStemCounterSearcher implements StemCounterSearcher {
	
	private RowSortedTable<String, String, Integer> stemCounterSnapshot;
	
	private Map<String, Integer> stemCountByFileNameSnapshot;
	
	private Path queries;
	
	private TextStemmer<String> stemmer;
	
	private MockSearchResultIndex index;
	
	private final String BRACKET_AND_COMMA_REGEX = "[\\[\\],]";
	
	
	public MockNewSimpleStemCounterSearcher(
			StemCounter stemCounter,
			Path queries,
			TextStemmer<String> stemmer,
			MockSearchResultIndex index) {
		this.stemCounterSnapshot = stemCounter.snapshotOfStemCountTable();
		this.stemCountByFileNameSnapshot = stemCounter.snapshotOfStemCountsByFile();
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
				.forEach(this::searchStemCounter);
		}
	}
	
	private Collection<String> safeUniqueStems(String line) {
		try {
			return stemmer.uniqueStems(line);
		}
		catch (Exception e) {
			System.err.println("Should NEVER RUN");
			throw new IllegalArgumentException();
		}
	}
	
	private void searchStemCounter(Collection<String> queryStems) {
		String queryAsLine = queryStems.toString()
				.replaceAll(BRACKET_AND_COMMA_REGEX, "");
		stemCounterSnapshot.columnMap().entrySet()
			.forEach(entry -> addResultToIndex(queryStems, queryAsLine, entry));
	}
	
	private void addResultToIndex(
			Collection<String> queryStems,
			String queryAsLine,
			Map.Entry<String, Map<String, Integer>> entry) {
		String where = entry.getKey();
		Map<String, Integer> stemCountPerStem = entry.getValue();
		double matches = queryStems.stream()
				.map(stem -> stemCountPerStem.get(stem))
				.filter(value -> value != null)
				.reduce(0, Integer::sum);
		double stemCount = stemCountByFileNameSnapshot.get(where);
		if (matches == 0 || stemCount == 0) { // We don't want any div by 0 errors or scores of 0 to show up in the index
			return;
		}
		index.add(queryAsLine, where, matches, matches/stemCount);
	}
}
	