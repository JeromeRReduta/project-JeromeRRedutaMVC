package text_stemming;

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

import opennlp.tools.stemmer.snowball.SnowballStemmer;
import opennlp.tools.stemmer.Stemmer;

public class TextFileStemmer implements TextStemmer<Path> {
	
    private final SnowballStemmer.ALGORITHM DEFAULT;

    public TextFileStemmer() {
    	this.DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;
    }
    
	@Override
	public ArrayList<String> listStems(Path textSource) throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(
				textSource,
				StandardCharsets.UTF_8)) {
			return stemTokenStream(
					TextParser.getTokenStream(reader),
					ArrayList::new);
		}
	}

	@Override
	public TreeSet<String> uniqueStems(Path textSource) throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(
				textSource,
				StandardCharsets.UTF_8)) {
			return stemTokenStream(
					TextParser.getTokenStream(reader),
					TreeSet::new);
		}
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
