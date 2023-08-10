package controllers.search_result_indexing;

import java.util.Map;
import java.util.Set;

import controllers.JsonWriteableController;
import data.search_result_indexing.SearchResultIndex.SearchResult;

public interface SearchResultIndexController
	extends JsonWriteableController {
	
	Map<String, ? extends Set<SearchResult>> snapshot();

}
