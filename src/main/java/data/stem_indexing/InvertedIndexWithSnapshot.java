package data.stem_indexing;

import java.util.TreeSet;

import com.google.common.collect.RowSortedTable;

public interface InvertedIndexWithSnapshot extends InvertedIndex {
	
	RowSortedTable<String, String, TreeSet<Integer>> snapshot();

}
