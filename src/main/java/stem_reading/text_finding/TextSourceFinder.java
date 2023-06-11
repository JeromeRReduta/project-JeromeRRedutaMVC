package stem_reading.text_finding;

import java.io.IOException;
import java.util.Collection;

/** Finds text sources for the TextStemmer to stem */
public interface TextSourceFinder<E> {
	
	/** Finds text sources from some data source. 
	 * @throws IOException */
	Collection<E> getTextSources() throws IOException;

}
