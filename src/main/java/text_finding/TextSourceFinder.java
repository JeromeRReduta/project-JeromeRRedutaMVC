package text_finding;

import java.util.Collection;

/** Finds text sources for the TextStemmer to stem */
public interface TextSourceFinder<E> {
	
	/** Finds text sources from some data source */
	Collection<E> getTextSources();
	
	
	

}
