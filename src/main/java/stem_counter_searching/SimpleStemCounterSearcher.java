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
		stemCounterSnapshot.columnMap().entrySet().stream()
			.map(entry -> new ExactQueryInfo(entry, queryStems))
			.filter(exactQueryInfo -> exactQueryInfo.matches > 0)
			.forEach(ExactQueryInfo::addInfoToIndex);
	}
	
	class ExactQueryInfo {
		
		private String fileName;

		private String queryAsLine;
		
		private double matches;
		
		private double score;
		
		private static final String BRACKET_REGEX = "[\\[\\]]";
		
		private ExactQueryInfo(
				Map.Entry<String, Map<String, Integer>> columnMapEntry,
				Collection<String> queryStems) {
			this.fileName = columnMapEntry.getKey();
			this.queryAsLine = queryStems.toString().replaceAll(BRACKET_REGEX, "");
			Map<String, Integer> stemsInFile = columnMapEntry.getValue();
			this.matches = 0;
			queryStems.forEach(stem -> safeIncrementMatches(stem, stemsInFile));
			this.score = this.matches/stemCountByFileNameSnapshot.get(this.fileName);
		}
		
		void safeIncrementMatches(String stem, Map<String, Integer> stemsInFile) {
			Integer numOfStemInFile = stemsInFile.get(stem);
			this.matches += numOfStemInFile != null
					? numOfStemInFile
					: 0;
		}
		
		void addInfoToIndex() {
			index.add(queryAsLine, fileName, score, matches);
		}
		
		@Override
		public String toString() {
			return String.format("{\n"
					+ "\tquery as line: %s\n"
					+ "\tmatches: %f\n"
					+ "\tscore: %f\n"
					+ "}\n",
					queryAsLine,
					matches,
					score);
					
		}
	}
}

	