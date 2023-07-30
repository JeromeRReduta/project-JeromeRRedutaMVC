package data.search_results;

import java.util.Collection;
import java.util.Map;

import json.JsonWriteable;

public interface SearchResultIndex extends JsonWriteable {
	
	default void add(
			String query,
			String fileName,
			double stemCount,
			double matches) {
		double score = matches/stemCount;
		SearchResult result = factory().create(fileName, stemCount, score);
		add(query, result);
	}
	
	void add(String query, SearchResult result);
	
	SearchResult.Factory factory();
	
	Map<String, ? extends Collection<SearchResult>> snapshot();
}
