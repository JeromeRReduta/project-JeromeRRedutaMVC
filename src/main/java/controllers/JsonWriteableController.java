package controllers;

/**
 * General class for any controller managing JsonWriteable data. It is recommended that when declaring
 * type, you should use the most general type that implements this class, e.g. use InvertedIndexController,
 * not JsonWriteableController. The exception to this is the display workflow, as it is specifically intended
 * to display ANY JsonWriteable.
 * @author JRRed
 *
 */
public interface JsonWriteableController {
	
	/**
	 * Tries to write data to some output source. The data, view object, output source,
	 * and error handling should all be managed by the implementation.
	 */
	void tryDisplaying();

}
