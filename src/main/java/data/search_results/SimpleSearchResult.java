package data.search_results;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import json.JsonWriter;

public class SimpleSearchResult implements SearchResult {
	
	private String where;
	
	private double matches;
	
	private double score;
	
	private SimpleSearchResult(String where, double matches, double score) {
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
	
	public static class Factory implements SearchResult.Factory {
		private String fileName;
		
		private Map<String, Integer> stemCountPerStem;
		private Integer totalStemsInFile;
		private Collection<String> queryStems;
		
		public Factory(
				String fileName,
				Map<String, Integer> stemCountPerStem,
				Integer totalStemsInFile,
				Collection<String> queryStems) {
			this.fileName = fileName;
			this.stemCountPerStem = stemCountPerStem;
			this.totalStemsInFile = totalStemsInFile;
			this.queryStems = queryStems;
		}
		
		@Override
		public SearchResult create() {
			double matches = calculateMatches(queryStems, stemCountPerStem);
			double score = calculateScore(matches, totalStemsInFile);
			return new SimpleSearchResult(fileName, matches, score);
		}

		private double calculateMatches(Collection<String> queryStems, Map<String, Integer> stemCountPerStem) {
			return queryStems.stream()
					.map(stem -> stemCountPerStem.get(stem))
					.filter(value -> value != null)
					.reduce(0, Integer::sum); // shouldn't make this a DoubleStream b/c it casts everything to primitive double, which will cause nullPointerException if the result is empty
		}
		
		private double calculateScore(double matches, Integer totalStemsInFile) {
			return totalStemsInFile != null
					? matches/(double)totalStemsInFile
					: SearchResult.NOT_FOUND_SCORE;
		}
	}
}
