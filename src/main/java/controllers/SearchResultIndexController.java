package controllers;

import java.util.Map;
import java.util.Set;

import data.search_results.SearchResultIndex.SearchResult;

public interface SearchResultIndexController
	extends JsonWriteableController {
	
	Map<String, ? extends Set<SearchResult>> snapshot();

}
