package configurations.modular_configs;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Map;

import argument_parsing.CommandLineReader;
import data.InvertedIndex;
import json.JsonMapWriter;
import stem_reading.StemReader;
import stem_reading.text_finding.TextSourceFinder;
import stem_reading.text_stemming.TextStemmer;
import stem_reading.text_finding.TextFileFinder;
import stem_reading.text_stemming.TextFileStemmer;
import stem_reading.TextFileStemReader;

import static configurations.StaticGlobalValues.*;

public class InvertedIndexDataReadingConfig implements ModularConfig {
	
	public final Path inputFile;
	
	public final TextSourceFinder<Path> textFileFinder;
	
	public final TextStemmer<Path> textFileStemmer;
	
	public final StemReader<Path> stemReader;
	
	public final InvertedIndex sharedIndex;
	
	public InvertedIndexDataReadingConfig(
			Path inputFile,
			TextSourceFinder<Path> textFileFinder,
			TextStemmer<Path> textFileStemmer,
			StemReader<Path> stemReader,
			InvertedIndex sharedIndex) {
		this.inputFile = inputFile;
		this.textFileFinder = textFileFinder;
		this.textFileStemmer = textFileStemmer;
		this.stemReader = stemReader;
		this.sharedIndex = sharedIndex;
	}

	@Override
	public void writeToJson(int baseIndent, Writer writer) throws IOException {
		Map<String, String> configAsMap = Map.of(
				"Input file", inputFile == null ? "(null)" : inputFile.toString(),
				"Text file finder", getClassString(textFileFinder),
				"Text file stemmer", getClassString(textFileStemmer),
				"StemReader", getClassString(stemReader),
				"Inverted Index", getClassString(sharedIndex));
		JsonMapWriter.writeMap(baseIndent, writer, configAsMap);
	}
	
	public String toString() {
		return toJsonString();
	}
	
	public static class Factory implements ModularConfig.Factory<InvertedIndexDataReadingConfig>{
		
		private final CommandLineReader reader;
		
		private final InvertedIndex sharedIndex;
		
		public Factory(CommandLineReader reader, InvertedIndex sharedIndex) {
			this.reader = reader;
			this.sharedIndex = sharedIndex;
		}

		@Override
		public InvertedIndexDataReadingConfig createConfig() {
			Path inputFile = reader.getPath(TEXT_SOURCE, null);
			TextSourceFinder<Path> textFileFinder = new TextFileFinder(inputFile);
			TextStemmer<Path> textFileStemmer = new TextFileStemmer();
			StemReader<Path> stemReader = new TextFileStemReader(sharedIndex, textFileFinder, textFileStemmer);
			return new InvertedIndexDataReadingConfig(
					inputFile,
					textFileFinder,
					textFileStemmer,
					stemReader,
					sharedIndex);
		}
	}
}
