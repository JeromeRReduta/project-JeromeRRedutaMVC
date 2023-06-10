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
	public String getString(String flag, String defaultValue);
	
	/** Returns the value mapped to the given flag as a Path, or defaultValue
	 * if no value exists or is null
	 * @param flag string
	 * @param defaultValue value to return if no value exists or is null
	 * @return the value mapped to the given flag as a Path, or defaultValue
	 * if no value exists or is null
	 */
	public Path getPath(String flag, Path defaultValue);
	
	/** Returns the value mapped to the given flag as an Integer, or defaultValue
	 * if no value exists or is null
	 * @param flag string
	 * @param defaultValue value to return if no value exists or is null
	 * @return the value mapped to the given flag as an Integer, or defaultValue
	 * if no value exists or is null
	 */
	public int getInteger(String flag, int defaultValue);
}
