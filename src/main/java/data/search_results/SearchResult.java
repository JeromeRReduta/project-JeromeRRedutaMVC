package data.search_results;

import json.JsonWriteable;

public interface SearchResult extends Cloneable, JsonWriteable {
	
	String where();
	
	double matches();
		
	double score();
	
	interface Factory {
		SearchResult create(String fileName, double stemCount, double matches);
	}

	SearchResult clone();
}
