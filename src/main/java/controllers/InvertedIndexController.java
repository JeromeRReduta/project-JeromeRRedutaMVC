package controllers;

import java.util.Collection;
import java.util.Map;

/**
 * Determines what an outside app can do with an InvertedIndex
 * (which may or may not be populated).
 * @author JRRed
 *
 */
public interface InvertedIndexController extends JsonWriteableController {
	
	/**
	 * Given a stem as key, returns the associated inner map value. To ensure data safety, this method
	 * should return a CLONE.
	 * @param stem
	 * @return a CLONE of the associated value
	 */
	Map<String, ? extends Collection<Integer>> get(String stem);
}
