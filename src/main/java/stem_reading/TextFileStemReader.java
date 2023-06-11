package stem_reading;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import data.InvertedIndex;
import stem_reading.text_finding.TextSourceFinder;
import stem_reading.text_stemming.TextStemmer;

public class TextFileStemReader implements StemReader<Path> {
	
	private InvertedIndex index;
	
	private TextSourceFinder<Path> finder;
	
	private TextStemmer<Path> stemmer;
	
	public TextFileStemReader(
			InvertedIndex index,
			TextSourceFinder<Path> finder,
			TextStemmer<Path> stemmer
			) {
		this.index = index;
		this.finder = finder;
		this.stemmer = stemmer;
	}
	
	@Override
	public void readIntoInvertedIndex(Path source) throws IOException {
		int position = 1;
		Collection<String> stems = stemmer.listStems(source);
		for (String stem : stems) {
			index.add(
					stem,
					source.toString(),
					position);
			position++;
		}
	}

	@Override
	public Collection<Path> getTextSources() throws IOException {
		return finder.getTextSources();
	}
}
