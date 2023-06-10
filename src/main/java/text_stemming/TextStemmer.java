package text_stemming;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

/** Given some text source, converts the text into a list of stems */
public interface TextStemmer<E> {
	
	/**
	 * Converts the text source into a list of stems
	 * @param textSource text source
	 * @return a list of stems, processed from the text source
	 * @throws IOException
	 */
	public ArrayList<String> listStems(E textSource) throws IOException;

	/**
	 * Converts the text source into a unique, sorted set of stems
	 * @param textSource text source
	 * @return a unique, sorted set of stems, processed from the text source
	 * @throws IOException
	 */
	public TreeSet<String> uniqueStems(E textSource) throws IOException;

}
