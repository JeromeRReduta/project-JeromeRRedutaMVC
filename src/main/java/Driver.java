import java.nio.file.Path;
import java.time.Duration;

import data.InvertedIndexTable;
import data.search_results.SearchResult;
import data.search_results.SearchResultIndex;
import data.search_results.SimpleSearchResult;
import data.search_results.SimpleSearchResultIndex;

import java.time.Instant;
import java.util.TreeSet;

import apps.App;
import apps.Project1App;
import apps.Project1AppWithWorkflows;
import argument_parsing.ArgumentMap;
import configurations.Project1Config;

import data.stem_counting.StemCounter;
import data.stem_counting.StemCounterTable;
import stem_counter_searching.SimpleStemCounterSearcher;
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
		Instant start = Instant.now(); // store initial start time
		Project1Config config = new Project1Config.Factory(args).createValidatedConfig();
		App app = new Project1AppWithWorkflows(config);
		app.run();
		
		
		StemCounter counter = new StemCounterTable();
		TableValueTransformer<TreeSet<Integer>, Integer> transformer = TreeSet::size;
		transformer.transform(((InvertedIndexTable) config.invertedIndex).snapshot(), (StemCounterTable)counter);

		SearchResultIndex index = new SimpleSearchResultIndex(SimpleSearchResult.FACTORY);
		var queries = new ArgumentMap(args).getPath("-query", null);
		var b = new SimpleStemCounterSearcher(counter, queries, config.textStemmer, index);
		b.trySearchingStemCounter();
		System.out.println(index);
		
		
		// calculate time elapsed and output
		Duration elapsed = Duration.between(start, Instant.now()); 
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
	}
}