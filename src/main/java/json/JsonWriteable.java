package json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/** An object that can write its own data in a JSON format
 * 
 * @author JRRed
 *
 */
public interface JsonWriteable {

	/**
	 * Writes its own data to JSON format
	 * @param baseIndent base indent
	 * @param writer writer
	 * @throws IOException for write()
	 */
	void writeToJson(int baseIndent, Writer writer) throws IOException;

	/**
	 * Writes its own data as a JSON-formatted string
	 * @return internal data as a JSON-formatted string
	 * @throws IOException for write()
	 */
	default String toJsonString() throws IOException {
		Writer writer = new StringWriter();
		writeToJson(0, writer);
		return writer.toString();
	}
}
