package controllers.stem_counting;

import java.util.Map;

import com.google.common.collect.RowSortedTable;

import controllers.JsonWriteableController;

public interface StemCounterController
	extends JsonWriteableController {
	
	RowSortedTable<String, String, Integer> stemCountTableSnapshot();
	
	Map<String, Integer> totalStemsByFileNameSnapshot();
}
