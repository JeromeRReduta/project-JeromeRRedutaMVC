package views;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import data.stem_counting.StemCounter;
import json.JsonMapWriter;
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
