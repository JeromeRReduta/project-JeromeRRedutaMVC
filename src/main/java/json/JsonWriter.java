package json;

import java.io.IOException;
import java.io.Writer;

/**
 * Writes an object's internal data in a JSON format
 */
public interface JsonWriter {

    /** String for Windows newline */
    static String crlf = "\r\n";

    /**
     * Writes a string with a given writer, at a given indent level
     * @param baseIndent indent level
     * @param writer writer
     * @param data string data
     * @throws IOException
     */
    static void writeIndented(int baseIndent, Writer writer, String data) throws IOException {
        String indentedData = "\t".repeat(baseIndent) + data;
        writer.write(indentedData);
    }
}
