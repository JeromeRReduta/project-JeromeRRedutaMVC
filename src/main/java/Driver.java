import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.TreeSet;
import apps.App;
import apps.Project1App;
import apps.Project1AppWithWorkflows;

import argument_parsing.ArgumentMap;
import configurations.Project1Config;
import data.InvertedIndexTable;
import data.search_results.SimpleSearchResult;
import data.search_results.SimpleSearchResultIndex;
import stem_reading.text_stemming.TextStemmer;
import table_value_transforming.TableValueTransformer;

import data.stem_counting.StemCounter;
import data.stem_counting.StemCounterTable;

import data.search_results.SearchResultIndex;

import stem_counter_searching.StemCounterSearcher;
import stem_counter_searching.SimpleStemCounterSearcher;

import stem_reading.text_stemming.TextLineStemmer;

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
		
		
		StemCounter counter = new StemCounterTable();
		TableValueTransformer<TreeSet<Integer>, Integer> transformer = TreeSet::size;
		transformer.transform(((InvertedIndexTable) config.invertedIndex).snapshot(), (StemCounterTable)counter);

		SearchResultIndex index = new SimpleSearchResultIndex(SimpleSearchResult.FACTORY);
		var queries = new ArgumentMap(args).getPath("-query", null);
		var b = new SimpleStemCounterSearcher(counter, queries, new TextLineStemmer(), index);
		b.trySearchingStemCounter();
		System.out.println(index);
		
		
		// calculate time elapsed and output
		Duration elapsed = Duration.between(start, Instant.now()); 
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
		
	}
}