import configurations.Config;

import data.InvertedIndex;
import data.SimpleInvertedIndex;
import stem_reading.StemReader;
import stem_reading.TextFileStemReader;
import text_finding.TextFileFinder;
import text_stemming.TextFileStemmer;
import views.InvertedIndexView;
import argument_parsing.ArgumentMap;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import configurations.Project1Config;

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
		// store initial start time
		Instant start = Instant.now();
		Project1Config config = Project1Config.Factory.createFromArgs(args);
		System.out.println(config);
		
		try {
			ArgumentMap map = new ArgumentMap(args);
			InvertedIndex index = new SimpleInvertedIndex();
			StemReader<Path> reader = new TextFileStemReader(
					index,
					new TextFileFinder(map.getPath("-text", null)),
					new TextFileStemmer());
			reader.readIntoInvertedIndex();
			InvertedIndexView view = new InvertedIndexView(
					index,
					map.getPath("-index", Path.of("index.json")));
			if (map.containsFlag("-index")) {
				view.writeToFile();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			
		}
		
		// calculate time elapsed and output
		Duration elapsed = Duration.between(start, Instant.now());
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
	}

	/*
	 * Generally, "Driver" classes are responsible for setting up and calling
	 * other classes, usually from a main() method that parses command-line
	 * parameters. Generalized reusable code are usually placed outside of the
	 * Driver class. They are sometimes called "Main" classes too, since they 
	 * usually include the main() method. 
	 * 
	 * If the driver were only responsible for a single class, we use that class
	 * name. For example, "TaxiDriver" is what we would name a driver class that
	 * just sets up and calls the "Taxi" class.
	 *
	 * TODO: Delete this after reading.
	 */
}
