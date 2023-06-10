package json;

import java.io.IOException;
import java.io.Writer;

/**
 * An object that can write its internal contents in a JSON format
 * @author JRRed
 *
 * @param <K> type of whatever element this object prints
 */
public interface JsonWriteable<K> {
	
	/**
	 * Writes the object's JSON representation
	 */
	void writeToJson(K element, Writer writer, int baseIndent) throws IOException;

}
