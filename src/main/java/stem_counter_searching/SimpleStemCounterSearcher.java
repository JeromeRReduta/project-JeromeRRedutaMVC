package stem_counter_searching;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;

import com.google.common.collect.RowSortedTable;

import data.search_results.SearchResultIndex;
import data.stem_counting.StemCounter;
import stem_reading.text_stemming.TextStemmer;

public class SimpleStemCounterSearcher implements StemCounterSearcher {
	
	private RowSortedTable<String, String, Integer> stemCounterSnapshot;
	
	private Map<String, Integer> stemCountByFileNameSnapshot;
	
	private Path queries;
	
	private TextStemmer<String> stemmer;
	
	private SearchResultIndex index;
	
	
	public SimpleStemCounterSearcher(
			StemCounter stemCounter,
			Path queries,
			TextStemmer<String> stemmer,
			SearchResultIndex index) {
		this.stemCounterSnapshot = stemCounter.snapshotOfStemCountTable();
		this.stemCountByFileNameSnapshot = stemCounter.snapshotOfStemCountsByFile();
		this.queries = queries;
		this.stemmer = stemmer;
		this.index = index;
	}

	@Override
	public void searchStemCounter() throws IOException {
		/*
		System.out.println(stemmer.uniqueStems(queries)); 
		stemmer.uniqueStems(queries)
			.stream()
			.forEach(queryLine ->
				stemCounterSnapshot.columnKeySet().forEach(fileName -> addNonZeroSearchResult(queryLine, fileName)));
		
		*/
		
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
		System.out.println(queryStems);
		/** TODO: Implement exact search here
		 * Then implement partial search
		 * Then make something to toggle between the two
		 */
	}
	
	private void addNonZeroSearchResult(String queryLine, String fileName) {
		SearchInfo info = new SearchInfo(queryLine, fileName, stemCounterSnapshot,
				stemCountByFileNameSnapshot);
		if (info.stemCount == 0) {
			return;
		}
		index.add(queryLine, fileName, info.stemCount, info.matches);
	}
	
	
	class SearchInfo {
		
		private String[] queryTokens;
		
		private double matches;
		
		private double stemCount;
		
		SearchInfo(
				String queryLine,
				String fileName,
				RowSortedTable<String, String, Integer> stemCounterSnapshot,
				Map<String, Integer> stemCountByFileNameSnapshot) {
			
			this.queryTokens = queryLine.split(" ");
			this.matches = stemCounterSnapshot.columnMap().entrySet().stream()
				.filter(this::queriesContainsStem)
				.mapToDouble(entry -> entry.getValue().get(fileName))
				.sum();
			System.out.println("MATCHERS: " + matches);
			
			Map<String, Integer> column = stemCounterSnapshot.column(fileName);
			Map<String, Double> matchCounts = new HashMap<>();
			for (String token : queryTokens) {
				matchCounts.put(token, (double)column.getOrDefault(token, 0));
			}
			System.out.println(column);
			System.out.println(Arrays.toString(queryTokens));
			System.out.println(matchCounts);
			
			/* Time: 0.112s */
			
			/* Time w/ hashmap approach */
			/*
			stemCounterSnapshot.columnMap().entrySet().stream()
				.filter(this::queriesContainsStem)
				*/
				/** map to values, then filter out null results */
			/*
				.mapToDouble(entry -> {
					String stem = entry.getKey();
					return stemCounterSnapshot.get(stem, fileName);
				})
				.forEach(System.out::println);
				*/
				
			this.stemCount = Optional.ofNullable(stemCountByFileNameSnapshot.get(fileName)).orElse(0);
		
		}
		/*
		private boolean queriesContainsStem(Map.Entry<String, Integer> stemMatchPair) {
			for (String query : queries) {
				if (query.equalsIgnoreCase(stemMatchPair.getKey())) {
					return true;
				}
			}
			return false;
		}
		*/
		
		private boolean queriesContainsStem(Map.Entry<String, Map<String, Integer>> entry) {
			for (String query : queryTokens) {
				if (query.equalsIgnoreCase(entry.getKey())) {
					return true;
				}
			}
			return false;
		}
	}
}

	