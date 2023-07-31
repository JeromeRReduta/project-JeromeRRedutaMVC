package json;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

public interface JsonCollectionWriter<E> extends JsonWriter {

    /**
     * Writes the collection's elements in Json format
     * @param baseIndent base indent level
     * @param writer writer
     * @param collection collection
     * @throws IOException from write()
     */
    default void writeAllElements(int baseIndent, Writer writer, Collection<E> collection) throws IOException {
        Iterator<E> iterator = collection.iterator();
        JsonWriter.writeIndented(0, writer, "[");
        if (iterator.hasNext()) {
            JsonWriter.writeIndented(0, writer, JsonWriter.crlf);
            writeElement(baseIndent + 1, writer, iterator.next());
        }
        while (iterator.hasNext()) {
            JsonWriter.writeIndented(0, writer, "," + JsonWriter.crlf);
            writeElement(baseIndent + 1, writer, iterator.next());
        }
        JsonWriter.writeIndented(0, writer, JsonWriter.crlf);
        JsonWriter.writeIndented(baseIndent, writer, "]");
    }

    /**
     * Writes a single element in this collection
     * @param baseIndent base indent
     * @param writer writer
     * @param element element
     * @throws IOException from write()
     */
    void writeElement(int baseIndent, Writer writer, E element) throws IOException;

    /**
     *
     * @param baseIndent Writes a generic collection. This will work with any collection, but if you're using a collection
     *                   of JsonWriteables, then writeJsonWriteableCollection is recommended instead.
     * @param writer writer
     * @param collection collection
     * @param <X> type of collection's elements
     * @throws IOException from write()
     */
    static <X> void writeCollection(int baseIndent, Writer writer, Collection<X> collection) throws IOException {
        JsonCollectionWriter<X> utility = (bI, w, e) -> JsonWriter.writeIndented(bI, w, e.toString());
        utility.writeAllElements(baseIndent, writer, collection);
    }

    /**
     *
     * @param baseIndent Writes a collection of JsonWriteables. This allows composite JSON writing, e.g. writing a List of Lists of Lists of Ints
     *                   so long as the implementations implement JsonWriteables.
     * @param writer writer
     * @param collection collection
     * @throws IOException from write()
     */
    static <J extends JsonWriteable> void writeJsonWriteableCollection(int baseIndent, Writer writer, Collection<J> collection) throws IOException {
        JsonCollectionWriter<J> utility = (bI, w, e) -> e.writeToJson(bI, w);
        utility.writeAllElements(baseIndent, writer, collection);
    }
}
