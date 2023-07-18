package controllers;

import java.util.TreeSet;

import com.google.common.collect.RowSortedTable;

public interface InvertedIndexControllerWithSnapshot extends JsonWriteableController {
	
	RowSortedTable<String, String, TreeSet<Integer>> snapshot();
}
