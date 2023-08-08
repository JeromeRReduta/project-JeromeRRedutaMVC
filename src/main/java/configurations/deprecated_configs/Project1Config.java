package configurations.deprecated_configs;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import argument_parsing.ArgumentMap;
import controllers.InvertedIndexController;
import controllers.TextFileInvertedIndexController;
import data.InvertedIndex;
import data.InvertedIndexTable;
import data.SimpleInvertedIndex;
import json.JsonMapWriter;
import stem_reading.StemReader;
import stem_reading.TextFileStemReader;
import stem_reading.text_finding.TextFileFinder;
import stem_reading.text_finding.TextSourceFinder;
import stem_reading.text_stemming.TextFileStemmer;
import stem_reading.text_stemming.TextStemmer;
import views.DataToTextFileView;
import views.InvertedIndexView;

/**
 * Project 1 Implementation of Config
 * @deprecated modular only now
 * @author JRRed
 *
 */
public class Project1Config implements Config {
	
	public final Path sourceFile;
	
	public final Path outputFile;
	
	public final InvertedIndex invertedIndex;
	
	public final DataToTextFileView invertedIndexView;
	
	public final InvertedIndexController invertedIndexController;
	
	public final TextSourceFinder<Path> textFinder;
	
	public final TextStemmer<Path> textStemmer;
	
	public final StemReader<Path> stemReader;
	
	private Project1Config(
			Path sourceFile,
			Path outputFile,
			InvertedIndex invertedIndex,
			DataToTextFileView invertedIndexView,
			InvertedIndexController invertedIndexController,
			TextSourceFinder<Path> textFinder,
			TextStemmer<Path> textStemmer,
			StemReader<Path> stemReader
			) {
		this.sourceFile = sourceFile;
		this.outputFile = outputFile;
		this.invertedIndex = invertedIndex;
		this.invertedIndexView = invertedIndexView;
		this.invertedIndexController = invertedIndexController;
		this.textFinder = textFinder;
		this.textStemmer = textStemmer;
		this.stemReader = stemReader;
	}
	
	@Override
	public void writeToJson(int baseIndent, Writer writer) throws IOException {
		JsonMapWriter.writeMap(0, writer, configAsMap());
	}
	
	private Map<String, Object> configAsMap() {
		try {
			return Map.of(
					"sourceFile", sourceFile,
					"outputFile", outputFile,
					"InvertedIndex class", invertedIndex.getClass().getSimpleName(),
					"DataToTextFileView class", invertedIndexView.getClass().getSimpleName(),
					"TextFinder class", textFinder.getClass().getSimpleName(),
					"TextStemmer class", textStemmer.getClass().getSimpleName(),
					"StemReader class", stemReader.getClass().getSimpleName());
		}
		catch (NullPointerException e) {
			return new HashMap<>();
		}
		catch (Exception e) {
			System.err.println("Should never run");
			assert false;
			return new HashMap<>();
		}
	}
	
	@Override
	public String toString() {
		try {
			return toJsonString();
		}
		catch (Exception e) {
			System.err.println("Should never run");
			return null;
		}
	}

	/**
	 * Factory implementation for this config
	 * @author JRRed
	 *
	 */
	public static class Factory implements Config.Factory<Project1Config> {
		
		private final ArgumentMap argMap;
	
		/** Statically assigned vars and dependencies - we choose these implementations because I said so */
		private final String sourceFlag = "-text";
		private final String outputFlag = "-index";
		private final Path defaultOutputFile = Path.of("index.json");
		private final Path noTextFlagOutputFile = null;
		private final TextStemmer<Path> stemmer = new TextFileStemmer();
		private final InvertedIndex index = new InvertedIndexTable();
		
		/** Dynamically assigned vars and dependencies - either chosen from commandline args or depending on something chosen from commandline args */
		private final Path sourceFile;
		private final Path outputFile;
		private final DataToTextFileView view;
		private final InvertedIndexController controller;
		private final TextSourceFinder<Path> finder;
		private final StemReader<Path> reader;
		
		/* Apparently if you don't set every var to final, the compiler
		 * won't complain if you forget to set it and leave it null
		 */
		public Factory(String[] args) {
			this.argMap = new ArgumentMap(args);
			this.sourceFile = argMap.getPath(sourceFlag, null);
			this.outputFile = argMap.containsFlag(outputFlag)
					? argMap.getPath(outputFlag, defaultOutputFile)
					: noTextFlagOutputFile;
			this.view = new InvertedIndexView(
					index,
					outputFile);
			this.controller = new TextFileInvertedIndexController(
					index,
					view);
			this.finder = new TextFileFinder(sourceFile);
			this.reader = new TextFileStemReader(
					index,
					finder,
					stemmer);
		}
		
		@Override
		public Project1Config createConfig() {
			return new Project1Config(
					sourceFile,
					outputFile,
					index,
					view,
					controller,
					finder,
					stemmer,
					reader);
		}
		
		/**
		 * Created only for testing Project1ConfigValidator. If you call this function the app will break. So like, don't.
		 * @return don't even
		 */
		public Project1Config createInvalid() {
			Project1Config config = new Project1Config(
					null,
					null,
					new SimpleInvertedIndex(),
					null,
					null,
					null,
					null,
					null);
			getValidator(config).validate();
			return config;
		}

		@Override
		public ConfigValidator<Project1Config> getValidator(Project1Config config) {
			return new Project1ConfigValidator(config);
		}
	}
}
