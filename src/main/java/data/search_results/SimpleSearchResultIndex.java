package data.search_results;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import data.search_results.SearchResult.Factory;
import json.JsonCollectionWriter;
import json.JsonMapWriter;
import json.JsonWriter;

public class SimpleSearchResultIndex implements SearchResultIndex {
	
	private Map<String, TreeSet<SearchResult>> searchResults;
	
	private SearchResult.Factory factory;
	
	public SimpleSearchResultIndex(SearchResult.Factory factory) {
		this.searchResults = new TreeMap<>();
		this.factory = factory;
	}

	@Override
	public void add(String query, SearchResult result) {
		searchResults.putIfAbsent(query, new TreeSet<SearchResult>());
		searchResults.get(query).add(result);
	}

	@Override
	public Factory factory() {
		return factory;
	}

	@Override
	public Map<String, ? extends Collection<SearchResult>> snapshot() {
		Map<String, TreeSet<SearchResult>> snapshot = new TreeMap<String, TreeSet<SearchResult>>();
		searchResults.forEach((query, treeSet) -> addTreeSetCloneToSnapshot(snapshot, query, treeSet));
		return snapshot;
	}
	
	private void addTreeSetCloneToSnapshot(
		Map<String, TreeSet<SearchResult>> snapshot,
		String query,
		TreeSet<SearchResult> treeSet) {
			treeSet.forEach(result -> {
				snapshot.putIfAbsent(query, new TreeSet<>());
				snapshot.get(query).add(result.clone());
			});
		}
	
	@Override
	public void writeToJson(int baseIndent, Writer writer) throws IOException {
		JsonMapWriter<String, TreeSet<SearchResult>> mapWriter = (bI, w, e) -> {
			JsonWriter.writeIndented(bI, w, "\"" + e.getKey() + "\": ");
			JsonCollectionWriter.writeCollection(bI, w, e.getValue());
		};
		mapWriter.writeAllElements(baseIndent, writer, searchResults);
	}
	
	@Override
	public String toString() {
		return toJsonString();
	}
}
