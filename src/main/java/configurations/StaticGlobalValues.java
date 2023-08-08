package configurations;

import java.nio.file.Path;

public final class StaticGlobalValues {
	
	public final static String TEXT_SOURCE = "-text";
	
	public final static String INVERTED_INDEX_OUTPUT = "-index";
	
	public final static Path DEFAULT_INVERTED_INDEX_OUTPUT = Path.of("index.json");
	
	public final static Path NO_TEXT_FLAG_INDEX_OUTPUT = null;

}
