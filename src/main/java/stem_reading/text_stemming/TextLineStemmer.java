package stem_reading.text_stemming;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Implementation of TextStemmer for a text source, given as a Path
 * @author JRRed
 *
 */
public class TextLineStemmer implements TextStemmer<String> {
	
    private final SnowballStemmer.ALGORITHM DEFAULT;

    public TextLineStemmer() {
    	this.DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;
    }

	@Override
	public ArrayList<String> listStems(String textSource) throws IOException {
		return stemTokenStream(
				TextParser.getTokenStream(textSource),
				ArrayList::new);
	}

	@Override
	public TreeSet<String> uniqueStems(String textSource) throws IOException {
		return stemTokenStream(
				TextParser.getTokenStream(textSource),
				TreeSet::new);
	}
	
	private <C extends Collection<String>> C stemTokenStream(
			Stream<String> tokenStream,
			Supplier<C> supplier) {
		Stemmer stemmer = new SnowballStemmer(this.DEFAULT);
		return tokenStream
				.map(token -> stemmer.stem(token).toString())
				.collect(Collectors.toCollection(supplier));
	}
}
