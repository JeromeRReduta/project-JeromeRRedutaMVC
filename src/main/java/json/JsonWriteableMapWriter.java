package json;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * Utility class for printing a map that holds key-JsonWriteable pairs.
 * @author JRRed
 *
 * @param <K> key type
 * @param <JsonWriteable> value type. Must extend JsonWriteable
 */
public class JsonWriteableMapWriter<K, V extends JsonWriteable> implements JsonDataStructUtility<Map.Entry<K, V>> {

    private String startingBrace = "{";

    private String endingBrace = "}";

    private Map<K, V> map;

    private Writer writer;

    private int indent;

    public JsonWriteableMapWriter(
            Map<K, V> map,
            Writer writer,
            int indent
            ) {
        this.map = map;
        this.writer = writer;
        this.indent = indent;
    }

    @Override
    public String getStartingBrace() {
        return startingBrace;
    }

    @Override
    public String getEndingBrace() {
        return endingBrace;
    }

    @Override
    public Writer getWriter() {
        return writer;
    }

    @Override
    public int getIndent() {
        return indent;
    }

    @Override
    public Iterable<Map.Entry<K, V>> getIterable() {
        return map.entrySet();
    }

    @Override
    public void writeElement(Map.Entry<K, V> element) throws IOException {
        K key = element.getKey();
        JsonWriteable value = element.getValue();
        writeIndented('"' + key.toString() + '"' + ": ", 1);
        value.writeToJson(writer, indent + 1);
    }
}
//nice