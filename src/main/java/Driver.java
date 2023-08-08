import java.io.IOException;
import configurations.modular_configs.ModularConfig;
import java.nio.file.Path;
import java.time.Duration;

import stem_counter_searching.StemCounterSearcher;
import stem_counter_searching.SimpleStemCounterSearcher;
import java.time.Instant;
import java.util.TreeSet;
import java.util.function.BiPredicate;

import apps.App;
import apps.Project1AppWithWorkflows;
import apps.Project2AppWithWorkflows;
import argument_parsing.ArgumentMap;
import configurations.deprecated_configs.Project1Config;
import configurations.modular_configs.InvertedIndexDataReadingConfig;
import configurations.modular_configs.ModularProject1Config;
import configurations.modular_configs.ModularProject2Config;
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
		ModularProject2Config config
			= new ModularProject2Config.Factory(args).createConfig();
		App app = new Project2AppWithWorkflows(config);
		
		
		/** TODO: Fix how snapshotting works:
		 * 	(x) 1. Make an inverted index that supports snapshots
		 * 	(x) 2. Have a data reader that takes references from an inverted index and a stem counter
		 *  (x) 3. When told to do a read command, it takes a snapshot of the invertedindex and transforms the values into the stem counter
		 * 	(x) 4. Make searcher have a reference to the stem counter, and take a snapshot only when asked to search
		 * 	5. Fix configs
		 */
		app.run();
		
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