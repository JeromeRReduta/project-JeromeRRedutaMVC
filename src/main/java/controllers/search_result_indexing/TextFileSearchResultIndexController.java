package controllers.search_result_indexing;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import models.search_result_indexing.SearchResultIndex;
import models.search_result_indexing.SearchResultIndex.SearchResult;
import views.GenericTextFileView;

/**
 * An implementation of the search result index controller for text files
 * @author JRRed
 *
 */
public class TextFileSearchResultIndexController implements SearchResultIndexController {
	
	private SearchResultIndex model;
	
	private GenericTextFileView<SearchResultIndex> view;
	
	public TextFileSearchResultIndexController(
			SearchResultIndex model,
			GenericTextFileView<SearchResultIndex> view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void tryDisplaying() {
		try {
			view.writeToFile();
		}
		catch (IOException e) {
			System.err.println("Error displaying search result index; cancelling write...");
		}
		catch (Exception e) {
			System.err.println("This should NEVER run - textfilesearchresultindexcontroller");
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, ? extends Set<SearchResult>> snapshot() {
		return model.snapshot();
	}
}
