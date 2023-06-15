package json;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

public interface JsonMapWriter<K, V> extends JsonWriter {

    /**
     * Writes all elements in this map.
     *
     * @param baseIndent base indent
     * @param writer     writer
     * @param map        map
     * @throws IOException from write()
     */
    default void writeAllElements(int baseIndent, Writer writer, Map<K, V> map)
            throws IOException {
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        JsonWriter.writeIndented(0, writer, "{");
        if (iterator.hasNext()) {
            JsonWriter.writeIndented(0, writer, JsonWriter.crlf);
            writeElement(baseIndent + 1, writer, iterator.next());
        }
        while (iterator.hasNext()) {
            JsonWriter.writeIndented(0, writer, "," + JsonWriter.crlf);
            writeElement(baseIndent + 1, writer, iterator.next());
        }
        JsonWriter.writeIndented(0, writer, JsonWriter.crlf);
        JsonWriter.writeIndented(baseIndent, writer, "}");
    }

    /**
     * Writes a single entry in this map
     *
     * @param baseIndent base indent
     * @param writer     writer
     * @param element    element
     * @throws IOException from write()
     */
    void writeElement(int baseIndent, Writer writer, Map.Entry<K, V> element) throws IOException;

    /**
     * Writes a generic map to JSON
     *
     * @param baseIndent base indent
     * @param writer     writer
     * @param map        map
     * @param <X>        key type
     * @param <Y>        value type
     * @throws IOException from write()
     */
    static <X, Y> void writeMap(int baseIndent, Writer writer, Map<X, Y> map) throws IOException {
        JsonMapWriter<X, Y> utility = (bI, w, e) -> {
            String key = e.getKey().toString();
            String value = e.getValue().toString();
            JsonWriter.writeIndented(bI, w, "\"" + key + "\": " + value);
        };
        utility.writeAllElements(baseIndent, writer, map);
    }

    /**
     * Writes a Map of JsonWriteables. This allows composite writes, e.g. writing a TreeMap of String-InnerMap pairs,
     * where the InnerMap is made of String-TreeSet(Int) pairs, so long as the InnerMap and TreeSet both
     * implement JsonWriteable
     *
     * @param baseIndent base indent
     * @param writer     writer
     * @param map        map
     * @param <X>        key type
     * @throws IOException from write()
     */
    static <X, Y extends JsonWriteable> void writeJsonWriteableMap(int baseIndent, Writer writer, Map<X, Y> map) throws IOException {
        JsonMapWriter<X, Y> utility = (bI, w, e) -> {
            String key = e.getKey().toString();
            JsonWriteable value = e.getValue();
            JsonWriter.writeIndented(bI, w, "\"" + key + "\": ");
            value.writeToJson(bI, w);
        };
        utility.writeAllElements(baseIndent, writer, map);
    }
}
