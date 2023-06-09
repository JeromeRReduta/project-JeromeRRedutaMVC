package json;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

public class JsonMapOfCollectionsWriter<A, B, X extends Collection<B>> implements
    JsonDataStructUtility<Map.Entry<A, X>> {

    private String startingBrace = "{";

    private String endingBrace = "}";

    private Map<A, X> mapOFCollections;

    private Writer writer;

    private int indent;

    public JsonMapOfCollectionsWriter(Map<A, X> mapOFCollections,
                               Writer writer,
                               int indent) {
        this.mapOFCollections = mapOFCollections;
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
    public Iterable<Map.Entry<A, X>> getIterable() {
        return mapOFCollections.entrySet();
    }

    @Override
    public void writeElement(Map.Entry<A, X> element) throws IOException {
        A key = element.getKey();
        X collection = element.getValue();
        writeIndented('"' + key.toString() + '"' + ": ", 1);
        var utility = new JsonCollectionWriter<B>(
                collection,
                writer,
                indent + 1);
        utility.writeAllElements();
    }
}
