import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;

import stem_counter_searching.StemCounterSearcher;
import stem_counter_searching.SimpleStemCounterSearcher;
import java.time.Instant;
import java.util.TreeSet;

import apps.App;
import apps.Project1AppWithWorkflows;
import argument_parsing.ArgumentMap;
import configurations.Project1Config;
import data.AbstractStringKeyTable;
import data.InvertedIndexTable;
import data.search_results.SearchResultIndex;
import data.search_results.SearchResultIndexMap;
import data.search_results.SearchResultIndex.SearchResult;
import data.search_results.SearchResultIndex.SearchResultFactory;
import data.stem_counting.StemCounter;
import data.stem_counting.StemCounterTable;
import stem_reading.text_stemming.TextLineStemmer;
import table_value_transforming.StringKeyTableValueTransformer;
import views.GenericTextFileView;
import controllers.StemCounterController;
import controllers.TextFileStemCounterController;
import data.search_results.SimpleSearchResultIndex;
import controllers.SearchResultIndexController;
import controllers.TextFileSearchResultIndexController;

/**
 * Class responsible for running this project based on the provided command-line
 * arguments. See the README for details.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Summer 2021
 */
public class Driver {

	/**
	 * Initializes the classes necessary based on the provided command-line
	 * arguments. This includes (but is not limited to) how to build or search an
	 * inverted index.
	 *
	 * @param args flag/value pairs used to start this program
	 */
	public static void main(String[] args) {
		/*
		 * Figured it out:
		 * 
		 * The stemmer works as intended
		 * I didn't actually understand how query reading and stemming was supposed to be done
		 * When reading stems for the index, we want to read the whole file at once
		 * 	- That way, we can keep track of the position of each stem as a whole
		 * When reading stems for the query file, we want to read the file LINE BY LINE
		 * 	- We don't need to keep track of position of each stem
		 * 	- We DO need to know what the joined query line looks like, and search based off each
		 * 			line
		 * 
		 * So in reality, need to change our searcher to read the query file line by line
		 */
		Instant start = Instant.now(); // store initial start time
		Project1Config config = new Project1Config.Factory(args).createValidatedConfig();
		App app = new Project1AppWithWorkflows(config);
		app.run();
		ArgumentMap mockMap = new ArgumentMap(args);
		StemCounter counter = new StemCounterTable();
		GenericTextFileView<StemCounter> stemCounterView = new GenericTextFileView<>(counter, mockMap.getPath("-counts", null));
		StemCounterController stemCounterController = new TextFileStemCounterController(counter, stemCounterView);
		
		StringKeyTableValueTransformer<TreeSet<Integer>, Integer> transformer = TreeSet::size;
		transformer.transform((AbstractStringKeyTable<TreeSet<Integer>>)config.invertedIndex, (AbstractStringKeyTable<Integer>)counter);
		System.out.println(mockMap.containsFlag("-counts"));
		if (mockMap.containsFlag("-counts")) {
			stemCounterController.tryDisplaying();
		}
		
		SearchResultIndex sRIndex = new SearchResultIndexMap();
		GenericTextFileView<SearchResultIndex> sRIView = new GenericTextFileView<>(sRIndex, mockMap.getPath("-results", null));
		SearchResultIndexController sRIController = new TextFileSearchResultIndexController(sRIndex, sRIView);
		
		if (mockMap.containsFlag("-query")) {
			StemCounterSearcher searcher = new SimpleStemCounterSearcher(
					counter,
					mockMap.getPath("-query", null),
					new TextLineStemmer(),
					sRIndex);
			searcher.trySearchingStemCounter();
		}
		if (mockMap.containsFlag("-results")) {
			sRIController.tryDisplaying();
		}
		
		/** TODO:
		 * 1. change mock search result, searcher, index to real versions
		 * 2. cardin query result doesn't show up but should - maybe b/c it's empty?
		 * 3. change "matches" in JSON representation to "count"
		 * 4. change where in variables to fileName
		 * 5. Get double formatting
		 * 6. Then can work on view, controller, etc.
		 */
		
		
		// calculate time elapsed and output
		Duration elapsed = Duration.between(start, Instant.now()); 
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
		
	}
}