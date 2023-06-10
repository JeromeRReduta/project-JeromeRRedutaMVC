package data;

import java.util.Collection;
import java.util.Map;

import json.JsonWriteable;

/**
 * A nested data structure. The outermost layer is a map that holds stem-innerMap pairs. The
 * innerMap holds fileName-Collection(int) pairs. It connects stems to the files that contain them, 
 * and connects those files to the positions the stems were found in. Note that in general, position is 1-based
 * (i.e. the first stem is in position 1)
 * @author JRRed
 *
 */

/*
 * An example might look like this:
 * {
 * 		"ab": {
 * 				john.txt: [
 * 							12,
 * 							14,
 * 							256
 * 						],
 * 				directory/buddy.dude: [
 * 							1,
 * 							69,
 * 							420
 * 				]
 * 			}
 * 		"z": {
 * 				directory/buddy.dude: [
 * 					2,
 * 					138,
 * 					840
 * 				]
 * 			}
 * }
 */
public interface InvertedIndex extends
	JsonWriteable<InvertedIndex>,
	Cloneable {
	
	/**
	 * Adds a stem, connected to the fileName, connected to the position
	 * @param stem
	 * @param fileName
	 * @param position
	 */
	void add(String stem, String fileName, int position);
	
	/**
	 * Given a stem as key, returns the associated inner map value. To ensure data safety, this method
	 * should return a CLONE.
	 * @param stem
	 * @return a CLONE of the associated value
	 */
	Map<String, ? extends Collection<Integer>> get(String stem);
}
