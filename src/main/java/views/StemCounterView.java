package views;

import java.io.IOException;
import java.nio.file.Path;

import json.JsonWriteable;

public class StemCounterView implements DataToTextFileView {
	
	private Path outputFile;	
	
	private JsonWriteable model;
	
	public StemCounterView(Path outputFile, JsonWriteable model) {
		this.outputFile = outputFile;
		this.model = model;
	}

	@Override
	public void writeToFile() throws IOException {
		writeToFile(outputFile, model);
		
	}
}
