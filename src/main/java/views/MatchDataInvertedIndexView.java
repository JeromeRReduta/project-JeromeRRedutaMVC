package views;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import data.StemCountInvertedIndex;
import json.JsonWriteable;

public interface MatchDataInvertedIndexView extends DataToTextFileView {
	
	default void writeStemCountsToFile(Path outputFile, StemCountInvertedIndex index) throws IOException {
		writeStemCountsToFile(outputFile, index.getStemCountsByFile());
	}
	
	void writeStemCountsToFile() throws IOException;
}
