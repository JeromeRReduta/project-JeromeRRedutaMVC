package workflows;

import controllers.JsonWriteableController;

/**
 * A workflow that writes some JsonWriteable to a file
 * @author JRRed
 */
public interface WriteJsonToFileWorkflow {
	
	/**
	 * If requested, attempts to write a JSONWriteable to a file.
	 * @param isRequested should we write?
	 * @param controller JsonWriteableController. Rare case where we want any and all JsonWriteableControllers.
	 */
	static void runIfRequested(boolean isRequested, JsonWriteableController controller) {
		if (isRequested) {
			controller.tryDisplaying();
		}
	}
}
