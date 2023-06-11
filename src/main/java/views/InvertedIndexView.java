package views;

import java.io.IOException;
import java.nio.file.Path;

import data.InvertedIndex;
import json.JsonWriteable;

public class InvertedIndexView implements DataToTextFileView {

	private Path outputFile;
	
	private JsonWriteable model;
	
	public InvertedIndexView(
			InvertedIndex index,
			Path outputFile) {
		this.model = index;
		this.outputFile = outputFile;
	}
	
	public void writeToFile() throws IOException {
		writeToFile(outputFile, model);
	}

}
