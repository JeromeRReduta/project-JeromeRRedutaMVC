package data;

import java.io.IOException;
import java.nio.file.Path;

import views.MatchDataInvertedIndexView;

public abstract class AbstractMatchDataInvertedIndexView implements MatchDataInvertedIndexView {
	
	private Path outputFile;
	
	private StemCountInvertedIndex index;
	
	public AbstractMatchDataInvertedIndexView(
			Path outputFile,
			StemCountInvertedIndex index) {
		this.outputFile = outputFile;
		this.index = index;
	}
	
	void writeStemCountsToFile() {
		writeStemCountsToFile(outputFile, index.getStemCountsByFile());
	}
	

}
