package json;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Table;

public interface JsonTableWriter<R, C, V> extends JsonWriter {
	
	/**
	 * Make TableWriter
	 * 	TableWriter = highest layer
	 * 	InnerMapWriter = lower one
	 * 	MapOfCollectionsWriter = even lower
	 * 	CollectionWriter as lowest
	 */
	
	//TODO: Implement this

	default void writeAllElements(int baseIndent, Writer writer, Table<R, C, V> table)
		throws IOException {
        rowMapWriter().writeAllElements(baseIndent, writer, table.rowMap());
	}
	
	default JsonMapWriter<R, Map<C, V>> rowMapWriter() {
		return (bI, w, e) -> {
			String key = e.getKey().toString();
			JsonWriter.writeIndented(bI,  w,  "\"" + key + "\": ");
			columnWriter().writeAllElements(bI, w, e.getValue());
		};
	}
	
	default JsonMapWriter<C, V> columnWriter() {
		return (bI, w, e) -> {
			String columnKey = e.getKey().toString();
			JsonWriter.writeIndented(bI,  w,  "\"" + columnKey + "\": ");
			writeValue(bI, w, e.getValue());
		};
	}
	
	void writeValue(int baseIndent, Writer writer, V value) throws IOException;
}
