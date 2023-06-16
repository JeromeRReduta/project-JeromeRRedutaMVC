package apps;

import java.nio.file.Path;

import configurations.Project1Config;
import controllers.InvertedIndexController;
import stem_reading.StemReader;
import workflows.DataReadingWorkflow;
import workflows.WriteJsonToFileWorkflow;

/**
 * Mock class created to make sure workflows work the way I think they do, and that they can be used
 * in place of manually writing "if isRequested controller.tryThis()". If it works, then we can simply
 * use the workflows, which makes the code more modular and hopefully a little less error-prone.
 * @author JRRed
 *
 */
public class Project1AppWithWorkflows implements App, DataReadingWorkflow, WriteJsonToFileWorkflow {
	private StemReader<Path> stemReader;
	
	private InvertedIndexController controller;
	
	private boolean readDataIsRequested;
	
	private boolean outputToFileIsRequested;
	
	public Project1AppWithWorkflows(Project1Config config) {
		this.stemReader = config.stemReader;
		this.controller = config.invertedIndexController;
		this.readDataIsRequested = config.sourceFile != null;
		this.outputToFileIsRequested = config.outputFile != null;
	}
	@Override
	public void run() {
		DataReadingWorkflow.runIfRequested(readDataIsRequested, stemReader);
		WriteJsonToFileWorkflow.runIfRequested(outputToFileIsRequested, controller);
	}
}
