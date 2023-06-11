package json;

import java.io.IOException;
import java.io.StringWriter;
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
	
	/**
	 * Writes object's own data as a JSON-formatted string
	 * @return the object's data as a JSON-formatted string
	 */
	default String toJsonString() {
		try {
			Writer writer = new StringWriter();
			writeToJson(writer, 0);
			return writer.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
