package models.search_result_indexing;

import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import json.JsonWriteable;
import json.JsonWriter;

/**
 * An index for mapping query lines (actually query collections joined into a String) to collections
 * of Search Results
 * @author JRRed
 *
 */
public interface SearchResultIndex extends JsonWriteable {
	
	/**
	 * Adds a single search result to the index
	 * @param queryAsLine a query collection as a line
	 * @param result a search result
	 */
	void add(String queryAsLine, SearchResult result);
	
	/** Note - not doing for each result, add(queryAsLine, result) b/c this is a nested data struct */
	/**
	 * Adds a collection of search results to the index. Note that this is because a nested data structure,
	 * we don't want to just do something like "foreach result add(queryAsLine, result)"
	 * @param queryAsLine a query collection as a line
	 * @param results search results
	 */
	void addAll(String queryAsLine, Collection<SearchResult> results);
	
	/**
	 * Returns a snapshot of the index's data
	 * @return A snapshot of the index's data
	 */
	Map<String, ? extends Set<SearchResult>> snapshot();

	/**
	 * The result of searching some data structure. This implementation is similar to Java's Point
	 * class - it's a final class that only holds data, so it's perfectly fine to have no encapsulation
	 * or abstraction here
	 * @author JRRed
	 *
	 */
	public final class SearchResult implements JsonWriteable, Cloneable, Comparable<SearchResult> {
		
		public final String fileName;
		
		public final int matches;
		
		public final double score;
		
		/**
		 * When comparing SearchResults, we compare first by highest score, then by most matches,
		 * and lastly by file name (case-insensitive)
		 */
		public static final Comparator<SearchResult> COMPARATOR = Comparator
				.comparing((SearchResult result) -> result.score)
				.thenComparing(result -> result.matches).reversed() // we reverse this comparator b/c we want higher matches on top
				.thenComparing(result -> result.fileName.toLowerCase());
		
		/**
		 * We want to represent doubles here to 8 places
		 */
		private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00000000");
		
		private SearchResult(String fileName, Number matches, Number score) {
			this.fileName = fileName;
			this.matches = matches.intValue();
			this.score = score.doubleValue();
		}
		
		@Override
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
	
	/**
	 * Factory pattern for SearchResult
	 * @author JRRed
	 *
	 */
	public final class SearchResultFactory {
		
		/**
		 * Creates a search result given a file name, a matchCount, and a stemCount. Note that we
		 * input a stemCount (not matches), so we have to calculate the score
		 * @param fileName fileName
		 * @param matches matches
		 * @param stemCount stemCount
		 * @return
		 */
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
