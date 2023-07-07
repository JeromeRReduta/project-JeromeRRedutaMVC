package data;

import java.util.Collection;
import java.util.Map;

public interface InvertedIndexWithSnapshot {
	/**
	 * Adds a stem, connected to the fileName, connected to the position
	 * @param stem
	 * @param fileName
	 * @param position
	 */
	void add(String stem, String fileName, int position);
	
	InvertedIndexWithSnapshot snapshot();
}
