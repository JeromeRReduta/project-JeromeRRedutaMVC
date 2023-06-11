package json;

import java.io.IOException;
import java.io.Writer;

/**
 * An object that can write its internal contents in a JSON format
 * @author JRRed
 */
public interface JsonWriteable {
	
	/**
	 * Writes object's own data as JSON representation
	 */
	void writeToJson(Writer writer, int baseIndent) throws IOException;

}
