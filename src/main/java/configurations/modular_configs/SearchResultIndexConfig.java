package configurations.modular_configs;

import static data_reading.search_result_indexing.StemCounterSearcher.StemMatchingStrategy.EXACT_MATCH;
import static data_reading.search_result_indexing.StemCounterSearcher.StemMatchingStrategy.PARTIAL_MATCH;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Map;

import configurations.argument_parsing.CommandLineReader;
import controllers.search_result_indexing.SearchResultIndexController;
import controllers.search_result_indexing.TextFileSearchResultIndexController;
import data.search_result_indexing.SearchResultIndex;
import data.search_result_indexing.SearchResultIndexMap;
import data.stem_counting.StemCounter;
import data_reading.search_result_indexing.SimpleStemCounterSearcher;
import data_reading.search_result_indexing.StemCounterSearcher;
import data_reading.search_result_indexing.StemCounterSearcher.StemMatchingStrategy;
import data_reading.stem_indexing.TextLineStemmer;
import data_reading.stem_indexing.TextStemmer;
import json.JsonMapWriter;
import views.GenericTextFileView;

public class SearchResultIndexConfig implements ModularConfig {
	
	public final StemCounter stemCounter;
	
	public final StemCounterSearcher searcher;

	public final SearchResultIndex model;
	
	public final GenericTextFileView<SearchResultIndex> view;

	public final SearchResultIndexController controller;
	
	public final boolean shouldDoExactSearch;
	
	public final TextStemmer<String> textLineStemmer;
	
	public final Path queryFile;
	
	public final Path outputFile;
	
	public SearchResultIndexConfig(
			StemCounter stemCounter,
			StemCounterSearcher searcher,
			SearchResultIndex model,
			GenericTextFileView<SearchResultIndex> view,
			SearchResultIndexController controller,
			boolean shouldDoExactSearch,
			TextStemmer<String> textLineStemmer,
			Path queryFile,
			Path outputFile) {
		this.stemCounter = stemCounter;
		this.searcher = searcher;
		this.model = model;
		this.view = view;
		this.controller = controller;
		this.shouldDoExactSearch = shouldDoExactSearch;
		this.textLineStemmer = textLineStemmer;
		this.queryFile = queryFile;
		this.outputFile = outputFile;
	}

	@Override
	public void writeToJson(int baseIndent, Writer writer) throws IOException {
		Map<String, String> configAsMap = Map.of(
				"StemCounter", getClassString(stemCounter),
				"SearchResultIndex", getClassString(model),
				"View", getClassString(view),
				"Controller", getClassString(controller),
				"Output File", outputFile == null ? "(null)" : outputFile.toString());
		JsonMapWriter.writeMap(baseIndent, writer, configAsMap);
	}
	
	public static class Factory implements ModularConfig.Factory<SearchResultIndexConfig> {

		private final CommandLineReader reader;
		
		private final StemCounter stemCounter;
		
		public Factory(
				CommandLineReader reader,
				StemCounter stemCounter) {
			this.reader = reader;
			this.stemCounter = stemCounter;
		}
		
		/** TODO: Complete SearchResultIndexConfig */

		@Override
		public SearchResultIndexConfig createConfig() {
			boolean shouldDoExactSearch = reader.containsFlag("-exact"); // TODO - make this EXACT FLAG A VALUE IN GLOBAL VALUES
			SearchResultIndex model = new SearchResultIndexMap();
			Path outputFile = reader.containsFlag("-results") // TODO - make -results and results.json GLOBAL VALUE
					? reader.getPath("-results", Path.of("results.json"))
					: null;
			GenericTextFileView<SearchResultIndex> view
				= new GenericTextFileView<>(model, outputFile);
			SearchResultIndexController controller
				= new TextFileSearchResultIndexController(model, view);
			StemMatchingStrategy matchingStrategy
			= shouldDoExactSearch ? EXACT_MATCH : PARTIAL_MATCH;
			Path queryFile = reader.getPath("-query", null); // TODO: Make query flag GLOBAL VALUE
			TextStemmer<String> stemmer = new TextLineStemmer();
			StemCounterSearcher searcher
				= new SimpleStemCounterSearcher(
						stemCounter,
						queryFile,
						stemmer,
						model,
						matchingStrategy);
			return new SearchResultIndexConfig(
					stemCounter,
					searcher,
					model,
					view,
					controller,
					shouldDoExactSearch,
					stemmer,
					queryFile,
					outputFile);
			
		}
	}

}
