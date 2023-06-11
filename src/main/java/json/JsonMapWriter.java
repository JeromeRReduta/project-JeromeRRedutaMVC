package json;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class JsonMapWriter<K, V> implements JsonDataStructUtility<Map.Entry<K, V>> {

    private String startingBrace = "{";

    private String endingBrace = "}";

    private Map<K, V> map;

    private Writer writer;

    private int indent;

    public JsonMapWriter(
            Map<K, V> map,
            Writer writer,
            int indent) {
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
        V value = element.getValue();
        writeIndented('"' + key.toString() + '"' + ": " + value.toString(), 1);
    }
}
