package argument_parsing;

import java.nio.file.Path;

/** Map that holds arguments */
public interface CommandLineReader {
	
	/** Parses commandline args and adds it to itself */
	void parse(String[] args);
	
	/** Returns the value mapped to the given flag as a string, or defaultValue
	 * if no value exists or is null
	 * @param flag string
	 * @param defaultValue value to return if no value exists or is null
	 * @return the value mapped to the given flag as a string, or defaultValue
	 * if no value exists or is null
	 */
	String getString(String flag, String defaultValue);
	
	/** Returns the value mapped to the given flag as a Path, or defaultValue
	 * if no value exists or is null
	 * @param flag string
	 * @param defaultValue value to return if no value exists or is null
	 * @return the value mapped to the given flag as a Path, or defaultValue
	 * if no value exists or is null
	 */
	Path getPath(String flag, Path defaultValue);
	
	/** Returns the value mapped to the given flag as an Integer, or defaultValue
	 * if no value exists or is null
	 * @param flag string
	 * @param defaultValue value to return if no value exists or is null
	 * @return the value mapped to the given flag as an Integer, or defaultValue
	 * if no value exists or is null
	 */
	int getInteger(String flag, int defaultValue);
	
	/**
	 * Returns whether the reader has a given, non-null value associated with a flag
	 * @param flag flag
	 * @return if the reader has a given flag
	 */
	boolean hasNonNullValue(String flag);
	
	/**
	 * Returns whether the reader contains a given flag, regardless of what the
	 * actual value is. This means that if the reader parsed -flag=null, then
	 * containsFlag(-flag) = true
	 * @param flag flag
	 * @return whether the reader contains a given flag, regardless of its value
	 */
	boolean containsFlag(String flag);
}
