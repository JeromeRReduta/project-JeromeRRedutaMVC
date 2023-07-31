import java.time.Duration;
import java.time.Instant;
import java.util.TreeSet;

import apps.App;
import apps.Project1AppWithWorkflows;
import argument_parsing.ArgumentMap;
import configurations.Project1Config;
import data.InvertedIndexTable;
import data.search_results.MockSearchResultIndex;
import data.search_results.MockSimpleSearchResultIndex;
import data.stem_counting.StemCounter;
import data.stem_counting.StemCounterTable;
import stem_counter_searching.MockNewSimpleStemCounterSearcher;
import stem_reading.text_stemming.TextLineStemmer;
import table_value_transforming.TableValueTransformer;

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
		String[] thing = new String[] {"a", "b"};
		app.run();
		
		
		StemCounter counter = new StemCounterTable();
		TableValueTransformer<TreeSet<Integer>, Integer> transformer = TreeSet::size;
		transformer.transform(((InvertedIndexTable) config.invertedIndex).snapshot(), (StemCounterTable)counter);

		
		MockSearchResultIndex index = new MockSimpleSearchResultIndex();
		var queries = new ArgumentMap(args).getPath("-query", null);
		var searcher = new MockNewSimpleStemCounterSearcher(
				counter,
				queries,
				new TextLineStemmer(),
				index);
		searcher.trySearchingStemCounter();
		System.out.println(index);
		
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