package configurations;

import java.nio.file.Path;

import argument_parsing.ArgumentMap;
import argument_parsing.CommandLineReader;

public class Project1Config extends Config {
	
	public final Path sourceFile;
	
	public final Path outputFile;
	
//	public final InvertedIndex invertedIndex;
//	
//	public final InvertedIndexView invertedIndexView;
//	
//	public final InvertedIndexController invertedIndexController;
//	
//	public final TextFinder textFinder;
//	
//	public final TextStemmer textStemmer;
//	
//	public final StemReader stemReader;
	
	private Project1Config(
			Path sourceFile,
			Path outputFile //,
//			InvertedIndex invertedIndex,
//			InvertedIndexView invertedIndexView,
//			InvertedIndexController invertedIndexController,
//			TextFinder textFinder,
//			TextStemmer textStemmer,
//			StemReader stemReader
			) {
		this.sourceFile = sourceFile;
		this.outputFile = outputFile;
//		this.invertedIndex = invertedIndex;
//		this.invertedIndexView = invertedIndexView;
//		this.invertedIndexController = invertedIndexController;
//		this.textFinder = textFinder;
//		this.textStemmer = textStemmer;
//		this.stemReader = stemReader;
	}
	
	@Override
	public String toString() {
		return String.format("Project 1 configs:\n"
				+ "\tSource file: %s\n"
				+ "\tOutput file: %s\n",
				/** TODO: Add the rest of the stuff
				 * 
				 */
				
				sourceFile.toString(),
				outputFile);
	}
	

	public static class Factory {
		
		private final static String sourceFlag = "-path";
		private final static String outputFlag = "-index";
		
		private final static Path defaultOutputFile = Path.of("index.json");
		
		/** Statically assigned dependencies - we choose these implementations because I said so */
		/** TODO: Move all statically defined implementations up here,
		 * 
		 * e.g. private final static InvertedIndex index = new SimpleInvertedIndex();
		 */

		
		
		/**
		 * Sets the following configs:
		 * source file, 
		 * output file, 
		 * stemReader,
		 * TextFinder,
		 * TextStemmer,
		 * InvertedIndex, 
		 * InvertedIndexView, 
		 * InvertedIndexController,
		 * 
		 * @param args
		 * @return
		 */
		public static Project1Config createFromArgs(String[] args) {
			CommandLineReader argMap = new ArgumentMap(args);
			Path sourceFile = argMap.getPath(sourceFlag, null);
			Path outputFile = argMap.getPath(outputFlag, defaultOutputFile);
			
//			InvertedIndex index = new SimpleInvertedIndex();
//			InvertedIndexView view = new TextFileInvertedIndexView(
//					index,
//					outputFile);
//			InvertedIndexController controller = new TextFileInvertedIndexController(
//					index,
//					view);
//			
//			TextFinder finder = new TextFileFinder();
//			TextStemmer stemmer = new TextFileStemmer();
//			StemReader reader = new TextFileStemReader(
//					finder,
//					stemmer);
//			
			return new Project1Config(
					sourceFile,
					outputFile //,
//					index,
//					view,
//					controller,
//					finder,
//					stemmer,
//					reader
					);
			
		}
	}
	

}


