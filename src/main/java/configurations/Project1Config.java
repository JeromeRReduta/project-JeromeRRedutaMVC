package configurations;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import argument_parsing.ArgumentMap;
import controllers.InvertedIndexController;
import controllers.TextFileInvertedIndexController;
import data.InvertedIndex;
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

public class Project1Config implements Config {
	
	/* TODO:
	 * 1. Finish SimpleInvertedIndex, MapOfMapsWriter, MapOfCollectionsWriter, CollectionsWriter (done - no composite json writers allowed though)
	 * 2. InvertedIndexView (done - changed to dataToTextFileView)
	 * 3. TextFileInvertedIndexView (done - changed to InvertedIndexView)
	 * 4. Test in Driver (eh - done)
	 * 5. InvertedIndexController, TextFileInvertedIndexController(x) 
	 * 6. Project1Config and Factory, ConfigWriter (done - did not do config writer but gave a list representation instead)
	 * 7. Test in Driver
	 * 8. App (abstract class)
	 * 		EXCEPTION HANDLING - find the highest-possible level abstraction and have it deal w/ any exceptions that get thrown
	 * 		It's probably App's responsibility - in that case, write down new App.run() with exception handling <-- do below option instead
	 * 		OR:
	 * 			Instead of just throwing exceptions, null check input for each method in App, e.g.: <-- do this, not exception handling
	 * 				populateIndex():
	 * 					if inputFile == null:
	 * 						return
	 * 					
	 * 				writeToFile():
	 * 					if outputFile == null;
	 * 						return
	 * 					if !shouldOutput:
	 * 						return
	 * 9. Project1App
	 * 10. Run tests
	 * 11. Debug & retest
	 * 12. Done!
	 */
	
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
	public void writeToJson(Writer writer, int baseIndent) throws IOException {
		var utility = new JsonMapWriter<>(
				configAsMap(),
				writer,
				0);
		utility.writeIndented("CONFIGS: ", 0);
		utility.writeAllElements();
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
		return toJsonString();
	}

	public static class Factory implements Config.Factory<Project1Config> {
		
		private ArgumentMap argMap;
	
		/** Statically assigned vars and dependencies - we choose these implementations because I said so */
		private final String sourceFlag = "-text";
		private final String outputFlag = "-index";
		private final Path defaultOutputFile = Path.of("index.json");
		private final Path noTextFlagOutputFile = null;
		private final TextStemmer<Path> stemmer = new TextFileStemmer();
		private final InvertedIndex index = new SimpleInvertedIndex();
		
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
			System.out.println(sourceFile);
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

		@Override
		public ConfigValidator<Project1Config> getValidator(Project1Config config) {
			return new Project1ConfigValidator(config);
		}
	}
}
