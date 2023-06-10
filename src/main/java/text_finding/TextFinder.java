package text_finding;

import java.io.IOException;
import java.util.Collection;

/** Finds text for the TextStemmer to stem */
public interface TextFinder<E> {
	
	/** Finds text from some source */
	Collection<E> getTextFrom(E source) throws IOException;
	

}
