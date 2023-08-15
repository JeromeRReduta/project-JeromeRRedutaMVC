package controllers.stem_counting;

import java.util.Map;

import com.google.common.collect.RowSortedTable;

import controllers.JsonWriteableController;

/**
 * A controller for the stem counter
 * @author JRRed
 *
 */
public interface StemCounterController extends JsonWriteableController {
	
	/**
	 * Returns a snapshot of the counter's stem / fileName / stemCount triplets
	 * @return A snapshot of the counter's stem / fileName / stemCount triplets
	 */
	RowSortedTable<String, String, Integer> stemCountTableSnapshot();
	
	/**
	 * Returns a snapshot of the counter's fileName / totalStemCount pais
	 * @return a snapshot of the counter's fileName / totalStemCount pais
	 */
	Map<String, Integer> totalStemsByFileNameSnapshot();
}
