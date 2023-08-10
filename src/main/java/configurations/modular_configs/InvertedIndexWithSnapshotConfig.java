package configurations.modular_configs;

import static configurations.StaticGlobalValues.DEFAULT_INVERTED_INDEX_OUTPUT;
import static configurations.StaticGlobalValues.INVERTED_INDEX_OUTPUT;
import static configurations.StaticGlobalValues.NO_TEXT_FLAG_INDEX_OUTPUT;
import static configurations.StaticGlobalValues.TEXT_SOURCE;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Map;

import configurations.argument_parsing.CommandLineReader;
import controllers.stem_indexing.InvertedIndexController;
import controllers.stem_indexing.TextFileInvertedIndexController;
import data.stem_indexing.InvertedIndexTable;
import data.stem_indexing.InvertedIndexWithSnapshot;
import data_reading.stem_indexing.StemReader;
import data_reading.stem_indexing.TextFileFinder;
import data_reading.stem_indexing.TextFileStemReader;
import data_reading.stem_indexing.TextFileStemmer;
import data_reading.stem_indexing.TextSourceFinder;
import data_reading.stem_indexing.TextStemmer;
import json.JsonMapWriter;
import views.DataToTextFileView;
import views.GenericTextFileView;

public class InvertedIndexWithSnapshotConfig implements ModularConfig {
	
	public final Path inputFile;
	
	public final TextSourceFinder<Path> textFileFinder;
	
	public final TextStemmer<Path> textFileStemmer;
	
	public final StemReader<Path> stemReader;
	
	public final InvertedIndexWithSnapshot model;
	
	public final DataToTextFileView view;
	
	public final InvertedIndexController controller;
	
	public final Path outputFile;
	
	
	private InvertedIndexWithSnapshotConfig(
			Path inputFile,
			TextSourceFinder<Path> textFileFinder,
			TextStemmer<Path> textFileStemmer,
			StemReader<Path> stemReader,
			InvertedIndexWithSnapshot invertedIndex,
			DataToTextFileView view,
			InvertedIndexController controller,
			Path outputFile) {
		this.inputFile = inputFile;
		this.textFileFinder = textFileFinder;
		this.textFileStemmer = textFileStemmer;
		this.stemReader = stemReader;
		this.model = invertedIndex;
		this.view = view;
		this.controller = controller;
		this.outputFile = outputFile;
	}
	
	@Override
	public void writeToJson(int baseIndent, Writer writer) throws IOException {
		Map<String, String> configAsMap = Map.of(
				"Input File", inputFile == null ? "(null)" : inputFile.toString(),
				"TextSourceFinder", getClassString(textFileFinder),
				"TextStemmer", getClassString(textFileStemmer),
				"StemReader", getClassString(stemReader),
				"InvertedIndex", getClassString(model),
				"View", getClassString(view),
				"Controller", getClassString(controller),
				"Output file", outputFile == null ? "(null)" : outputFile.toString());
		JsonMapWriter.writeMap(baseIndent, writer, configAsMap);
	}
	
	public static class Factory implements ModularConfig.Factory<InvertedIndexWithSnapshotConfig> {
		
		private CommandLineReader reader;
		
		public Factory(
				CommandLineReader reader) {
			this.reader = reader;
		}

		@Override
		public InvertedIndexWithSnapshotConfig createConfig() {
			Path inputFile = reader.getPath(TEXT_SOURCE, null);
			InvertedIndexWithSnapshot model = new InvertedIndexTable();
			TextSourceFinder<Path> textFileFinder = new TextFileFinder(inputFile);
			TextStemmer<Path> textFileStemmer = new TextFileStemmer();
			StemReader<Path> stemReader = new TextFileStemReader(
					model, textFileFinder, textFileStemmer);
			Path outputFile = reader.containsFlag(INVERTED_INDEX_OUTPUT)
					? reader.getPath(INVERTED_INDEX_OUTPUT, DEFAULT_INVERTED_INDEX_OUTPUT)
					: NO_TEXT_FLAG_INDEX_OUTPUT;
			DataToTextFileView view
					= new GenericTextFileView<>(model, outputFile);
			InvertedIndexController controller
				= new TextFileInvertedIndexController(model, view);
			return new InvertedIndexWithSnapshotConfig(
					inputFile,
					textFileFinder,
					textFileStemmer,
					stemReader,
					model,
					view,
					controller,
					outputFile);
		}
	}
}
