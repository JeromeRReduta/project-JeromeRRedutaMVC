package data.search_result_indexing;

import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import json.JsonWriteable;
import json.JsonWriter;

public interface SearchResultIndex extends JsonWriteable {
	
	void add(String queryAsLine, SearchResult result);
	
	/** Note - not doing for each result, add(queryAsLine, result) b/c this is a nested data struct */
	void addAll(String queryAsLine, Collection<SearchResult> results);
	
	Map<String, ? extends Set<SearchResult>> snapshot();

	/** TODO: Figure out how to add queryAsLine to SearchResultIndex, even if there are no SearchResults - maybe putIfAbs? */
	
	public final class SearchResult implements JsonWriteable, Cloneable, Comparable<SearchResult> {
		
		public final String fileName;
		
		public final int matches;
		
		public final double score;
		
		public static final Comparator<SearchResult> COMPARATOR = Comparator
				.comparing((SearchResult result) -> result.score)
				.thenComparing(result -> result.matches).reversed() // we reverse this comparator b/c we want higher matches on top
				.thenComparing(result -> result.fileName.toLowerCase());
		
		private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00000000");
				// for some reason if I don't declare the class name here it won't compile
		
		private SearchResult(String fileName, Number matches, Number score) {
			this.fileName = fileName;
			this.matches = matches.intValue();
			this.score = score.doubleValue();
		}
		
		public SearchResult clone() {
			return new SearchResult(fileName, matches, score);
		}

		@Override
		public void writeToJson(int baseIndent, Writer writer) throws IOException {
			JsonWriter.writeIndented(baseIndent, writer, "{" + JsonWriter.crlf);
			JsonWriter.writeIndented(baseIndent + 1, writer, "\"where\": \"" + fileName + "\"," + JsonWriter.crlf);
			JsonWriter.writeIndented(baseIndent + 1, writer, "\"count\": " + matches + "," +  JsonWriter.crlf);
			JsonWriter.writeIndented(baseIndent + 1, writer, "\"score\": " + DECIMAL_FORMAT.format(score) + JsonWriter.crlf);
			JsonWriter.writeIndented(baseIndent, writer, "}");
		}
		
		@Override
		public int compareTo(SearchResult o) {
			return COMPARATOR.compare(this, o);
		}
		
		@Override
		public String toString() {
			return toJsonString();
		}
	}
	
	public final class SearchResultFactory {
		
		public static SearchResult create(
				String fileName,
				Number matches,
				Number stemCount) {
			if (matches == null || stemCount == null) {
				return null;
			}
			double matchesAsDouble = matches.doubleValue();
			double stemCountAsDouble = stemCount.doubleValue();
			if (matchesAsDouble == 0 || stemCountAsDouble == 0) {
				return null;
			}
			return new SearchResult(
					fileName,
					matchesAsDouble,
					matchesAsDouble/stemCountAsDouble);
			
		}
	}
}
