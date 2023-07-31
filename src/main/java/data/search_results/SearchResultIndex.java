package data.search_results;

import java.util.Collection;
import java.util.Map;

import json.JsonWriteable;

public interface SearchResultIndex extends JsonWriteable {
	
	void add(String query, SearchResult result);
	
	Map<String, ? extends Collection<SearchResult>> snapshot();
}
