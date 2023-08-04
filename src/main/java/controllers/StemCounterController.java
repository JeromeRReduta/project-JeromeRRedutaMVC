package controllers;

import java.util.Map;

import com.google.common.collect.RowSortedTable;

public interface StemCounterController
	extends JsonWriteableController {
	
	RowSortedTable<String, String, Integer> stemCountTableSnapshot();
	
	Map<String, Integer> totalStemsByFileNameSnapshot();
}
