package json;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

public class JsonCollectionWriter<E> implements JsonDataStructUtility<E> {

    private String startingBrace = "[";
    private String endingBrace = "]";
    private Collection<E> collection;
    private Writer writer;

    private int indent;

    public JsonCollectionWriter(Collection<E> collection,
                                Writer writer,
                                int indent) {
        this.collection = collection;
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
    public Iterable<E> getIterable() {
        return collection;
    }

    @Override
    public void writeElement(E element) throws IOException {
        writeIndented(element.toString(), 1);
    }
}
