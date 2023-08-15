package views;

import java.io.IOException;
import java.nio.file.Path;

import json.JsonWriteable;

/**
 * A generic text file view for any JsonWriteable model
 * @author JRRed
 *
 * @param <E> model type
 */
public final class GenericTextFileView<E extends JsonWriteable> implements DataToTextFileView {
	
	private JsonWriteable model;
	
	private Path writeDestination;
	
	public GenericTextFileView(E model, Path writeDestination) {
		this.model = model;
		this.writeDestination = writeDestination;
	}

	@Override
	public void writeToFile() throws IOException {
		writeToFile(writeDestination, model);
	}
}
