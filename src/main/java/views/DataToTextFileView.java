package views;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import json.JsonWriteable;

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
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
