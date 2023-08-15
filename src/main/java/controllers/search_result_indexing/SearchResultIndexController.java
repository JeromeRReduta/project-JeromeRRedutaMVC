package controllers.search_result_indexing;

import java.util.Map;
import java.util.Set;

import controllers.JsonWriteableController;
import models.search_result_indexing.SearchResultIndex.SearchResult;

/**
 * Controller for a search result index
 * @author JRRed
 *
 */
public interface SearchResultIndexController extends JsonWriteableController {
	
	/**
	 * Returns a snapshot of the search result index's state
	 * @return A snapshot of the search result index
	 */
	Map<String, ? extends Set<SearchResult>> snapshot();
}
