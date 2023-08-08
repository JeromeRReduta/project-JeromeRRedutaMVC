package apps;

import java.nio.file.Path;

import configurations.modular_configs.ModularProject2Config;
import controllers.InvertedIndexController;
import controllers.SearchResultIndexController;
import controllers.StemCounterController;
import stem_counter_searching.StemCounterSearcher;
import stem_reading.StemReader;
import table_value_transforming.InvertedIndexSnapshotCounter;
import workflows.Workflows;

public class Project2AppWithWorkflows implements
	App {
	
	private final boolean readTextFileIsRequested;
	
	private final boolean writeIndexIsRequested;
	
	private final StemReader<Path> stemReader;
	
	private final InvertedIndexController invertedIndexController;
	
	private final boolean countIndexStemsIsRequested;
	
	private final boolean writeStemCounterIsRequested;
	
	private final InvertedIndexSnapshotCounter snapshotCounter;
	
	private final StemCounterController stemCounterController;
	
	private final boolean searchStemCounterIsRequested;
	
	private final boolean writeSearchResultIndexIsRequested;
	
	private final StemCounterSearcher searcher;
	
	private final SearchResultIndexController searchResultIndexController;

	
	public Project2AppWithWorkflows(ModularProject2Config config) {
		this.readTextFileIsRequested
			= config.forInvertedIndex.inputFile != null;
		this.writeIndexIsRequested
			= config.forInvertedIndex.outputFile != null;
		this.stemReader = config.forInvertedIndex.stemReader;
		this.invertedIndexController = config.forInvertedIndex.controller;
		this.countIndexStemsIsRequested = true; // This is a default value - we always want to get the stem counter
		this.writeStemCounterIsRequested
			= config.forStemCounter.outputFile != null;
		this.searchStemCounterIsRequested
			= config.forSearchResults.queryFile != null;
		this.writeSearchResultIndexIsRequested
			= config.forSearchResults.outputFile != null;
		this.snapshotCounter = config.forStemCounter.invertedIndexSnapshotCounter;
		this.stemCounterController = config.forStemCounter.controller;
		this.searcher = config.forSearchResults.searcher;
		this.searchResultIndexController = config.forSearchResults.controller;
	}
	
	@Override
	public void run() {
		Workflows.ReadIntoInvertedIndex
			.runIfRequested(readTextFileIsRequested, stemReader);
		Workflows.DisplayIndex
			.runIfRequested(writeIndexIsRequested, invertedIndexController);
		Workflows.ReadIntoStemCounter
			.runIfRequested(countIndexStemsIsRequested, snapshotCounter);
		Workflows.DisplayStemCounter
			.runIfRequested(writeStemCounterIsRequested, stemCounterController);
		Workflows.ReadIntoSearchResultIndex
			.runIfRequested(searchStemCounterIsRequested, searcher);
		Workflows.DisplaySearchResultIndex
			.runIfRequested(writeSearchResultIndexIsRequested, searchResultIndexController);
	}
}
