package stem_counter_searching;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;

import com.google.common.collect.RowSortedTable;
import data.stem_counting.StemCounter;
import stem_reading.text_stemming.TextStemmer;

public class MockNewSimpleStemCounterSearcher 
	//implements StemCounterSearcher
	{
	/**
	
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
		TreeSet<MockSearchResultIndex.MockSearchResult> results
			= new TreeSet<>();
		stemCounterSnapshot.columnMap().entrySet().stream()
			.map(entry -> mapEntryToSearchResult(queryStems, entry))
			.filter(result -> result.score() > 0 && result.matches() > MockSearchResult.NOT_FOUND_SCORE)
			.forEach(result -> index.add(queryAsLine, result));
			
		stemCounterSnapshot.columnMap().entrySet()
			.forEach(entry -> addResultToIndex(queryStems, queryAsLine, entry));
	}
	
	private MockSearchResult mapEntryToSearchResult(
			Collection<String> queryStems,
			Map.Entry<String, Map<String, Integer>> entry) {
		String fileName = entry.getKey();
		Map<String, Integer> stemCountPerStem = entry.getValue();
		double matches = queryStems.stream()
				.map(stem -> stemCountPerStem.get(stem))
				.filter(value -> value != null)
				.reduce(0, Integer::sum);
		double stemCount = stemCountByFileNameSnapshot.getOrDefault(fileName, 0);
		double score = stemCount > 0.0 ? matches/stemCount : MockSearchResult.NOT_FOUND_SCORE;
		return resultFactory.create(fileName, matches, score);
	}
	private void addResultToIndex(
			Collection<String> queryStems,
			String queryAsLine,
			Map.Entry<String, Map<String, Integer>> entry) {
		String where = entry.getKey();

		index.add(queryAsLine, where, matches, matches/stemCount);
	}
	
	*/
}
	