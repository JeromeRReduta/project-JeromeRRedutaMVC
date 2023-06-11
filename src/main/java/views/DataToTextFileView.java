package views;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import json.JsonWriteable;

/**
 * A view of some model which displays its data by writing to some text file
 * @author JRRed
 *
 */
public interface DataToTextFileView {
	
	/**
	 * Writes the object in a JSON representation to a file. 
	 * This should call writeToFile(Path, JsonWriteable)
	 */
	void writeToFile() throws IOException;
	
	/**
	 * Default logic for writing to file
	 * @param outputFile
	 */
	default void writeToFile(Path outputFile, JsonWriteable model) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(
				outputFile,
				StandardCharsets.UTF_8)) {
			model.writeToJson(
					writer,
					0);
		}
	}
}
