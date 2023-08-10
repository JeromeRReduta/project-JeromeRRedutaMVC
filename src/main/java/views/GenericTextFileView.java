package views;

import java.io.IOException;
import java.nio.file.Path;

import data.stem_indexing.InvertedIndex;
import json.JsonWriteable;

public final class GenericTextFileView<E extends JsonWriteable>
	implements DataToTextFileView {
	
	private JsonWriteable model;
	
	private Path writeDestination;
	
	
	
	public GenericTextFileView(
			E model, Path writeDestination) {
		this.model = model;
		this.writeDestination = writeDestination;
	}

	@Override
	public void writeToFile() throws IOException {
		writeToFile(writeDestination, model);
	}
}
