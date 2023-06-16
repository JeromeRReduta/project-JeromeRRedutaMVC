import java.time.Duration;
import java.time.Instant;

import apps.App;
import apps.Project1App;
import apps.Project1AppWithWorkflows;
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
		Instant start = Instant.now(); // store initial start time
		Project1Config config = new Project1Config.Factory(args).createValidatedConfig();
		App app = new Project1AppWithWorkflows(config);
		app.run();
		// calculate time elapsed and output
		Duration elapsed = Duration.between(start, Instant.now()); 
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
	}
}