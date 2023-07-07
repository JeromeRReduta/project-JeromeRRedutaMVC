package controllers;

public interface InvertedIndexControllerWithSnapshot {
	
	/**
	 * Writes the index to an output. The index and the output source
	 * are handled by the implementation.
	 */
	void tryDisplayingIndex();
	
	InvertedIndexControllerWithSnapshot snapshot();
}
