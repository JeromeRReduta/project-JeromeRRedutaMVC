package configurations;

import text_finding.TextSourceFinder;
import json.JsonCollectionWriter;
import views.DataToTextFileView;
import views.InvertedIndexView;
import text_finding.TextFileFinder;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;

import argument_parsing.ArgumentMap;
import argument_parsing.CommandLineReader;
import controllers.InvertedIndexController;
import controllers.TextFileInvertedIndexController;
import text_stemming.TextStemmer;
import text_stemming.TextFileStemmer;
import stem_reading.StemReader;
import stem_reading.TextFileStemReader;
import data.InvertedIndex;
import data.SimpleInvertedIndex;

public class Project1Config extends Config {
	
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
	
	public final boolean shouldProduceOutputFile;
	
	public final InvertedIndex invertedIndex;
	
	public final DataToTextFileView invertedIndexView;
	
	public final InvertedIndexController invertedIndexController;
	
	public final TextSourceFinder<Path> textFinder;
	
	public final TextStemmer<Path> textStemmer;
	
	public final StemReader<Path> stemReader;
	
	private Project1Config(
			Path sourceFile,
			Path outputFile,
			boolean shouldProduceOutputFile,
			InvertedIndex invertedIndex,
			DataToTextFileView invertedIndexView,
			InvertedIndexController invertedIndexController,
			TextSourceFinder<Path> textFinder,
			TextStemmer<Path> textStemmer,
			StemReader<Path> stemReader
			) {
		this.sourceFile = sourceFile;
		this.outputFile = outputFile;
		this.shouldProduceOutputFile = shouldProduceOutputFile;
		this.invertedIndex = invertedIndex;
		this.invertedIndexView = invertedIndexView;
		this.invertedIndexController = invertedIndexController;
		this.textFinder = textFinder;
		this.textStemmer = textStemmer;
		this.stemReader = stemReader;
	}
	
	@Override
	public void writeToJson(Writer writer, int baseIndent) throws IOException {
		var utility = new JsonCollectionWriter<>(
				configAsList(),
				writer,
				0);
		utility.writeIndented("CONFIGS: ", 0);
		utility.writeAllElements();
	}
	
	/**
	 * Represents this data as a list of attributes
	 * @return a list representation of the config's data
	 */
	private List<String> configAsList() {
		return List.of(
				"sourceFile: " + sourceFile,
				"outputFile: " + outputFile,
				"shouldProduceOutputFile: " + shouldProduceOutputFile,
				"InvertedIndex class: " + invertedIndex.getClass().getSimpleName(),
				"DataToTextFileView class: " + invertedIndexView.getClass().getSimpleName(),
				"InvertedIndexController class: " + invertedIndexController.getClass().getSimpleName(),
				"TextFinder class: " + textFinder.getClass().getSimpleName(),
				"TextStemmer class: " + textStemmer.getClass().getSimpleName(),
				"Stem Reader class: " + stemReader.getClass().getSimpleName());
	}
	
	@Override
	public String toString() {
		return toJsonString();
	}

	public static class Factory {
		
		/** Statically assigned vars and dependencies - we choose these implementations because I said so */
		private final static String sourceFlag = "-text";
		private final static String outputFlag = "-index";
		private final static Path defaultOutputFile = Path.of("index.json");
		private final static TextStemmer<Path> stemmer = new TextFileStemmer();
		private final static InvertedIndex index = new SimpleInvertedIndex();

		/**
		 * Sets up the configs. This should be the only way to create a new Project1Config.
		 * 
		 * @param args commandline args
		 * @return a Project1Config
		 */
		public static Project1Config createFromArgs(String[] args) {
			CommandLineReader argMap = new ArgumentMap(args);
			Path sourceFile = argMap.getPath(sourceFlag, null);
			Path outputFile = argMap.getPath(outputFlag, defaultOutputFile);
			boolean shouldProduceOutputFile = argMap.hasNonNullValue(outputFlag);
			DataToTextFileView view = new InvertedIndexView(
					index,
					outputFile);
			InvertedIndexController controller = new TextFileInvertedIndexController(
					index,
					view);
			TextSourceFinder<Path> finder = new TextFileFinder(sourceFile);
			StemReader<Path> reader = new TextFileStemReader(
					index,
					finder,
					stemmer);
			return new Project1Config(
					sourceFile,
					outputFile,
					shouldProduceOutputFile,
					index,
					view,
					controller,
					finder,
					stemmer,
					reader);
		}
	}
}
