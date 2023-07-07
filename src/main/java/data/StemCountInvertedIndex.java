package data;

import java.util.Collection;
import java.util.Map;

import json.JsonWriteable;

/**
 * An inverted index mapping stem / fileName / stem count triplets. It's very similar to how InvertedIndex works,
 * except this index tracks stem counts, rather than a treeset of stems
 * @author JRRed
 *
 */
public interface StemCountInvertedIndex extends JsonWriteable {
	
	/**
	 * Adds a stem / fileName / stem count triplet
	 * @param stem stem
	 * @param fileName fileName
	 * @param stemCount stem count
	 */
	void add(String stem, String fileName, int stemCount);
	
	/**
	 * Returns a cloned map of fileName / total stem count pairs
	 * @return a cloned map of fileName / total stem count pairs
	 */
	Map<String, Integer> getStemCountsByFile();
}
