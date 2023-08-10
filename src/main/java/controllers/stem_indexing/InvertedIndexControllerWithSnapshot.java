package controllers.stem_indexing;

import java.util.TreeSet;

import com.google.common.collect.RowSortedTable;

import controllers.JsonWriteableController;

public interface InvertedIndexControllerWithSnapshot extends JsonWriteableController {
	
	RowSortedTable<String, String, TreeSet<Integer>> snapshot();
}
