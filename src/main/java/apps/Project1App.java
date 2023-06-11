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
	
	private boolean canReadData;
	
	private boolean canDisplayIndexToFile;
	
	public Project1App(Project1Config config) {
		this.stemReader = config.stemReader;
		this.controller = config.invertedIndexController;
		this.canReadData = config.sourceFile != null;
		this.canDisplayIndexToFile = config.outputFile != null;
	}

	@Override
	public void run() {
		if (canReadData) {
			stemReader.tryReadingIntoInvertedIndex();
		}
		if (canDisplayIndexToFile) {
			controller.tryDisplayingIndex();
		}
	}
}

