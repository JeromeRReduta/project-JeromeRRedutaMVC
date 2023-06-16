package apps;

import java.nio.file.Path;

import configurations.Project1Config;
import controllers.InvertedIndexController;
import stem_reading.StemReader;

/**
 * App logic for project 1
 * @author JRRed
 *
 */
public class Project1App implements App {
	
	private StemReader<Path> stemReader;
	
	private InvertedIndexController controller;
	
	private boolean readDataIsRequested;
	
	private boolean outputToFileIsRequested;
	
	public Project1App(Project1Config config) {
		this.stemReader = config.stemReader;
		this.controller = config.invertedIndexController;
		this.readDataIsRequested = config.sourceFile != null;
		this.outputToFileIsRequested = config.outputFile != null;
	}

	@Override
	public void run() {
		if (readDataIsRequested) {
			stemReader.tryReadingIntoInvertedIndex();
		}
		if (outputToFileIsRequested) {
			controller.tryDisplaying();
		}
	}
}
