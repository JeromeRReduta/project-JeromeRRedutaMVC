package controllers;

import java.util.Collection;
import java.util.Map;

import data.StemCountInvertedIndex;

/**
 * Determines what an outside app can do with a StemCountInvertedIndexController
 * @author JRRed
 *
 */
public interface StemCountInvertedIndexController {

	/**
	 * Tries to display the controller's index, using its own view. Handling view logic is the implementation's
	 * responsibility
	 */
	void tryDisplayingIndex();
	
	/**
	 * Tries to display a map of how many stems each file has, using its own view. Handling view logic is the implementation's
	 * responsibility
	 */
	void tryDisplayingStemCountsByFile();
	
	/**
	 * Returns a snapshot (i.e. clone) of the index
	 * @return a snapshot/clone of the index
	 */
	StemCountInvertedIndex snapshot();
}
