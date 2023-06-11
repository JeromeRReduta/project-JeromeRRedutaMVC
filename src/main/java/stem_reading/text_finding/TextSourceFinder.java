package stem_reading.text_finding;

import java.io.IOException;
import java.util.Collection;

/**
 * Finds text sources for the TextStemmer to stem
 * @author JRRed
 *
 * @param <E> type of text source
 */
public interface TextSourceFinder<E> {
	
	/**
	 * Finds text sources from same data source
	 * @return Collection of text sources
	 * @throws IOException
	 */
	Collection<E> getTextSources() throws IOException;
}
