package data.search_results;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import json.JsonCollectionWriter;
import json.JsonMapWriter;
import json.JsonWriter;

public class MockSimpleSearchResultIndex implements MockSearchResultIndex {
	
	private Map<String, TreeSet<MockSearchResult>> searchResults;
	
	public MockSimpleSearchResultIndex() {
		this.searchResults = new TreeMap<>();
	}

	@Override
	public void writeToJson(int baseIndent, Writer writer) throws IOException {
		JsonMapWriter<String, TreeSet<MockSearchResult>> mapWriter = (bI, w, e) -> {
			JsonWriter.writeIndented(bI, w, "\"" + e.getKey() + "\": ");
			JsonCollectionWriter.writeJsonWriteableCollection(baseIndent + 1, writer, e.getValue());
		};
		mapWriter.writeAllElements(baseIndent, writer, searchResults);
		
	}
	

	@Override
	public void add(
			String queryAsLine,
			String where,
			double matches,
			double score) {
		searchResults.putIfAbsent(queryAsLine, new TreeSet<>());
		searchResults.get(queryAsLine).add(
			new MockSimpleSearchResult(where, matches, score));
		
	}

	@Override
	public Map<String, ? extends Collection<MockSearchResult>> snapshot() {
		Map<String, TreeSet<MockSearchResult>> snapshot = new TreeMap<>();
		searchResults.forEach((query, treeSet) -> {
			snapshot.putIfAbsent(query, new TreeSet<>());
			TreeSet<MockSearchResult> treeSetClone = snapshot.get(query);
			treeSet.stream()
				.map(result -> result.clone())
				.forEach(treeSetClone::add);
		});
		return snapshot;
	}
	
	@Override
	public String toString() {
		return toJsonString();
	}
	
	private class MockSimpleSearchResult implements MockSearchResultIndex.MockSearchResult {
		
		private String where;
		
		private double matches;
		
		private double score;
		
		private MockSimpleSearchResult(String where, double matches, double score) {
			this.where = where;
			this.matches = matches;
			this.score = score;
		}
		
		@Override
		public String where() {
			return where;
		}
		
		@Override
		public double matches() {
			return matches;
		}
		
		@Override
		public double score() {
			return score;
		}
		
		@Override
		public void writeToJson(int baseIndent, Writer writer) throws IOException {
			JsonWriter.writeIndented(baseIndent, writer, "{" + JsonWriter.crlf);
			JsonWriter.writeIndented(baseIndent + 1, writer, "where: \"" + where + "\"" + JsonWriter.crlf);
			JsonWriter.writeIndented(baseIndent + 1, writer, "matches: " + matches + JsonWriter.crlf);
			JsonWriter.writeIndented(baseIndent + 1, writer, "score: " + score + JsonWriter.crlf);
			JsonWriter.writeIndented(baseIndent, writer, "}");
		}
		
		@Override
		public String toString() {
			return toJsonString();
		}
		
		@Override
		public MockSimpleSearchResult clone() {
			return new MockSimpleSearchResult(where, matches, score);
		}
	}
}
