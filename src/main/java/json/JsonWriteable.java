package json;

import java.io.IOException;
import java.io.Writer;

/** Allows writing a JSON representation through some writer
 * 
 * @author JRRed
 *
 * @param <K>
 */
public interface JsonWriteable<K> {
	
	/**
	 * 
	 */
	void writeToJson(K element,
					Writer writer,
					int baseIndent) throws IOException;

}
