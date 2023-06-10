package json;


import java.io.IOException;
import java.io.Writer;

/** Utility class for writing common data structs to JSON */
public interface JsonDataStructUtility<E> {

    final static String comma = ",";
    final static String crlf = "\r\n";

    /** returns the implementation's starting brace */
    String getStartingBrace();
    
    /** returns the implementation's ending brace */
    String getEndingBrace();

    /** returns the implementation's writer */
    Writer getWriter();

    /** returns the implementation's base indent level */
    int getIndent();

    /**
     * Returns an interable for writeAllElements(Iterable)
     */
    Iterable<E> getIterable();

    /** writes an element with a given indent level */
    default void writeIndented(String element, int offset) throws IOException {
        Writer writer = getWriter();
        int indent = getIndent();
        writer.write("\t".repeat(indent + offset));
        writer.write(element);
    }

    /** writes all given elements through the writer */
    default void writeAllElements(Iterable<E> iterable) throws IOException {
        Writer writer = getWriter();
        var it = iterable.iterator();
        String startingBrace = getStartingBrace();
        String endingBrace = getEndingBrace();

        writer.write(startingBrace);
        if (it.hasNext()) {
            writer.write(crlf);
            var next = it.next();
            writeElement(next);
        }
        while (it.hasNext()) {
            writer.write(comma);
            writer.write(crlf);
            var next = it.next();
            writeElement(next);
        }
        writer.write(crlf);
        writeIndented(endingBrace, 0);
    }
    
    /** writes all given elements - this should provide the iterable
     * to writeAllElements(Iterable)
     * @throws IOException
     */
    default void writeAllElements() throws IOException {
        Iterable<E> iterable = getIterable();
        writeAllElements(iterable);
    }
    
    /** Writes a single element */
    void writeElement(E element) throws IOException;
}