package views;

import java.io.IOException;
import java.nio.file.Path;

import json.JsonWriteable;
import models.stem_indexing.InvertedIndex;

/**
 * Implementation of DataToTextFileView, for displaying an InvertedIndex
 * @deprecated We use GenericTextFileView now
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
