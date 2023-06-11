package stem_reading;

import java.nio.file.Path;
import java.util.Collection;

/**
 * Reads data (in the form of some text source converted into stems) into an InvertedIndex. 
 * The text source, index, and exception handling are the responsibility of the implementation.
 * @author JRRed
 *
 */
public interface StemReader<E> {
	
	/**
	 * Reads all given data (processed into stems) into an InvertedIndex. The index and 
	 * data source must be managed by the implementation.
	 */
	default void readIntoInvertedIndex() {
		try {
			Collection<E> textSources = getTextSources();
			for (E source : textSources) {
				readIntoInvertedIndex(source);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads one data source into an InvertedIndex. The index must be managed by the 
	 * implementation.
	 * @param source
	 */
	void readIntoInvertedIndex(E source) throws Exception;
	
	/**
	 * Gets text sources from a given data source. The data source 
	 * must be managed by the implementation.
	 * @return
	 */
	Collection<E> getTextSources() throws NullPointerException;
}
