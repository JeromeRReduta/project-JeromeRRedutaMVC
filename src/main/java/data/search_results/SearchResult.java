package data.search_results;

import java.util.Comparator;

import json.JsonWriteable;

public interface SearchResult extends Cloneable, Comparable<SearchResult>, JsonWriteable {
	
	String where();
	
	double matches();
		
	double score();
	
	static final double NOT_FOUND_SCORE = -1.0;
	
	SearchResult clone();
	
	static final Comparator<SearchResult> COMPARATOR = Comparator
			.comparing(SearchResult::score)
			.thenComparing(SearchResult::matches)
			.thenComparing(
					(a, b) -> a.where().compareToIgnoreCase(b.where()));
	
	@Override
	default int compareTo(SearchResult o) {
		return COMPARATOR.compare(this, o);
	}
	
	static interface Factory {
		SearchResult create();
	}
}
