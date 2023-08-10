package views;

import java.io.IOException;
import java.nio.file.Path;

import data.stem_indexing.InvertedIndex;
import json.JsonWriteable;

/**
 * Implementation of DataToTextFileView, for displaying an InvertedIndex
 * @author JRRed
 *
 */
public class InvertedIndexView implements DataToTextFileView {

	private Path outputFile;
	
	private JsonWriteable model;
	
	public InvertedIndexView(
			InvertedIndex index,
			Path outputFile
			) {
		this.model = index;
		this.outputFile = outputFile;
	}
	
	@Override
	public void writeToFile() throws IOException {
		writeToFile(outputFile, model);
	}
}
