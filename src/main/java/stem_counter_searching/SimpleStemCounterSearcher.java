package stem_counter_searching;

import java.io.IOException;
import java.nio.file.Path;
import java.util.TreeSet;

import com.google.common.collect.RowSortedTable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import data.search_results.SearchResultIndex;
import data.stem_counting.StemCounter;
import stem_reading.text_stemming.TextStemmer;

public class SimpleStemCounterSearcher implements StemCounterSearcher {
	
	private RowSortedTable<String, String, Integer> stemCounterSnapshot;
	
	private Map<String, Integer> stemCountByFileNameSnapshot;
	
	private Path queries;
	
	private TextStemmer<Path> stemmer;
	
	private SearchResultIndex index;
	
	
	public SimpleStemCounterSearcher(
			StemCounter stemCounter,
			Path queries,
			TextStemmer<Path> stemmer,
			SearchResultIndex index) {
		this.stemCounterSnapshot = stemCounter.snapshotOfStemCountTable();
		this.stemCountByFileNameSnapshot = stemCounter.snapshotOfStemCountsByFile();
		this.queries = queries;
		this.stemmer = stemmer;
		this.index = index;
	}

	@Override
	public void searchStemCounter() throws IOException {
		stemmer.uniqueStems(queries).forEach(this::searchStemCounter);
	}
	
	private void searchStemCounter(String query) {
		Arrays.stream(query.split(" "))
			/*.forEach( ??? )*/ ;

		/** How do we run a generic search on the stem counter? Given a three-stem query, what does searching look like? */
	}
	
	

}
