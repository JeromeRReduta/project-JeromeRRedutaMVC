package configurations;

import text_finding.TextSourceFinder;
import views.DataToTextFileView;
import views.InvertedIndexView;
import text_finding.TextFileFinder;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

import argument_parsing.ArgumentMap;
import argument_parsing.CommandLineReader;
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
	 * 5. InvertedIndexController, TextFileInvertedIndexController
	 * 6. Project1Config and Factory, ConfigWriter
	 * 7. Test in Driver
	 * 8. App (abstract class)
	 * 9. Project1App
	 * 10. Run tests
	 * 11. Debug & retest
	 * 12. Done!
	 */
	
	public final Path sourceFile;
	
	public final Path outputFile;
	
	public final InvertedIndex invertedIndex;
	
	public final DataToTextFileView invertedIndexView;
//	
//	public final InvertedIndexController invertedIndexController;
	
	public final TextSourceFinder<Path> textFinder;
	
	public final TextStemmer<Path> textStemmer;
	
	public final StemReader<Path> stemReader;
	
	private Project1Config(
			Path sourceFile,
			Path outputFile,
			InvertedIndex invertedIndex,
			DataToTextFileView invertedIndexView,
//			InvertedIndexController invertedIndexController,
			TextSourceFinder<Path> textFinder,
			TextStemmer<Path> textStemmer,
			StemReader<Path> stemReader
			) {
		this.sourceFile = sourceFile;
		this.outputFile = outputFile;
		this.invertedIndex = invertedIndex;
		this.invertedIndexView = invertedIndexView;
//		this.invertedIndexController = invertedIndexController;
		this.textFinder = textFinder;
		this.textStemmer = textStemmer;
		this.stemReader = stemReader;
	}
	
	@Override
	public void writeToJson(Writer writer, int baseIndent) throws IOException {
		
		/* TODO: implement Project1ConfigWriter so I can uncomment this */
//		var utility = new Project1ConfigWriter(
//				this,
//				writer,
//				baseIndent);
//		utility.writeAllElements();
	}
	
	@Override
	public String toString() {
		return toJsonString();
	}
	

	public static class Factory {
		
		/** Statically assigned vars and dependencies - we choose these implementations because I said so */
		private final static String sourceFlag = "-path";
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
			DataToTextFileView view = new InvertedIndexView(
					index,
					outputFile);
//			InvertedIndexController controller = new TextFileInvertedIndexController(
//					index,
//					view);
			TextSourceFinder<Path> finder = new TextFileFinder(sourceFile);
			StemReader<Path> reader = new TextFileStemReader(
					index,
					finder,
					stemmer);
			return new Project1Config(
					sourceFile,
					outputFile,
					index,
					view,
//					controller,
					finder,
					stemmer,
					reader
					);
		}
	}



	

}


