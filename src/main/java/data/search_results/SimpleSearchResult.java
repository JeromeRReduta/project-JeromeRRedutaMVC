package data.search_results;

import java.io.IOException;
import java.io.Writer;

import json.JsonWriter;

public class SimpleSearchResult implements SearchResult {
	
	private String where;
	
	private double matches;
	
	private double score;
	
	public static final SearchResult.Factory FACTORY = SimpleSearchResult::new;
	
	private SimpleSearchResult(
			String where,
			double matches,
			double score) {
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
	public SimpleSearchResult clone() {
		return new SimpleSearchResult(where, matches, score);
	}

	@Override
	public int compareTo(SearchResult o) {
		return COMPARATOR.compare(this, o);
	}
}
