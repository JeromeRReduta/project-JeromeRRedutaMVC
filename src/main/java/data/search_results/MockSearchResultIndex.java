package data.search_results;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import json.JsonWriteable;

public interface MockSearchResultIndex extends JsonWriteable {
	
	void add(
			String queryAsLine,
			String where,
			double matches,
			double score);
	
	Map<String, ? extends Collection<MockSearchResult>> snapshot();
	
	public interface MockSearchResult extends Cloneable, Comparable<MockSearchResult>, JsonWriteable {
		
		String where();
		
		double matches();
		
		double score();
		
		static final double NOT_FOUND_SCORE = -1.0;
		
		MockSearchResult clone();
		
		static final Comparator<MockSearchResult> COMPARATOR = Comparator
				.comparing(MockSearchResult::score)
				.thenComparing(MockSearchResult::matches)
				.thenComparing(
						(a, b) -> a.where().compareToIgnoreCase(b.where()));
		
		@Override
		default int compareTo(MockSearchResult o) {
			return COMPARATOR.compare(this, o);
		}

	}
}
