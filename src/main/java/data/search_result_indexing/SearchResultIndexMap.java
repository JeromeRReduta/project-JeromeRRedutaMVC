package data.search_result_indexing;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import json.JsonCollectionWriter;
import json.JsonMapWriter;
import json.JsonWriteable;
import json.JsonWriter;

public class SearchResultIndexMap
	extends TreeMap<String, TreeSet<SearchResultIndex.SearchResult>>
	implements SearchResultIndex, JsonWriteable {

	/**
	 *  Unused
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void add(String queryAsLine, SearchResult result) {
		super.putIfAbsent(queryAsLine, new TreeSet<>());
		if (result == null) {
			return;
		}
		super.get(queryAsLine).add(result);
		
	}

	@Override
	public void addAll(String queryAsLine, Collection<SearchResult> results) {
		super.putIfAbsent(queryAsLine, new TreeSet<>());
		Collection<SearchResult> data = super.get(queryAsLine);
		results.stream()
			.filter(result -> result != null)
			.forEach(data::add);
	}

	@Override
	public void writeToJson(int baseIndent, Writer writer) throws IOException {
		JsonMapWriter<String, TreeSet<SearchResult>> mapWriter = (bI, w, e) -> {
			JsonWriter.writeIndented(bI, w, "\"" + e.getKey() + "\": ");
			JsonCollectionWriter.writeJsonWriteableCollection(
					baseIndent + 1, writer, e.getValue());
		};
		mapWriter.writeAllElements(baseIndent, writer, this);
	}
	
	@Override
	public String toString() {
		return toJsonString();
	}

	@Override
	public Map<String, ? extends Set<SearchResult>> snapshot() {
		Map<String, TreeSet<SearchResult>> snapshot = new TreeMap<>();
		super.forEach((queryAsLine, results) -> 
			snapshot.put(queryAsLine, new TreeSet<>(results))); // Since SearchResults are final, immutable collections of data, it's totally fine to not clone them. We do, however, need to clone the TreeSet to maintain data integrity
		return snapshot;
	}
}
