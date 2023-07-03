package json;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.google.common.collect.Table;

/**
 * Utility class for writing a table in a "pretty" JSON format
 * @author JRRed
 *
 * @param <R> row key type
 * @param <C> column key type
 * @param <V> value type
 */
public interface JsonTableWriter<R, C, V> extends JsonWriter {

	/**
	 * Writes all the elements in a given table. In this implementation, we represent the table
	 * as a rowMap, then call a rowMapWriter to write that rowMap, which calls a columnWriter to write
	 * each column in the rowMap, which writes the value in each column. How the values are written depends
	 * on the implementation.
	 * @param baseIndent base indent
	 * @param writer writer
	 * @param table table
	 * @throws IOException thrown by writer
	 */
	default void writeAllElements(int baseIndent, Writer writer, Table<R, C, V> table)
		throws IOException {
        rowMapWriter().writeAllElements(baseIndent, writer, table.rowMap());
	}
	
	/**
	 * Utility writer for writing a table's rowMap
	 * @return a utility writer for writing a table's rowMap
	 */
	default JsonMapWriter<R, Map<C, V>> rowMapWriter() {
		return (bI, w, e) -> {
			String rowKey = e.getKey().toString();
			JsonWriter.writeIndented(bI,  w,  "\"" + rowKey + "\": ");
			columnWriter().writeAllElements(bI, w, e.getValue());
		};
	}
	
	/**
	 * Utility writer for writing a column / value mapping
	 * @return a utility writer for writing a column / value mapping
	 */
	default JsonMapWriter<C, V> columnWriter() {
		return (bI, w, e) -> {
			String columnKey = e.getKey().toString();
			JsonWriter.writeIndented(bI,  w,  "\"" + columnKey + "\": ");
			writeValue(bI, w, e.getValue());
		};
	}
	
	/**
	 * Writes a value in a table. Because the value might be any type, the class using this table writer (or some
	 * utility class working with it) should decide how that value is written.
	 * @param baseIndent base indent
	 * @param writer writer
	 * @param value value
	 * @throws IOException thrown by writer
	 */
	void writeValue(int baseIndent, Writer writer, V value) throws IOException;
}
