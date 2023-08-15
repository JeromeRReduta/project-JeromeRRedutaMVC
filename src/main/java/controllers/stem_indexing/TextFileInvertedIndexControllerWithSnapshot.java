package controllers.stem_indexing;

import java.io.IOException;
import java.util.TreeSet;

import com.google.common.collect.RowSortedTable;

import models.stem_indexing.InvertedIndex;
import models.stem_indexing.InvertedIndexWithSnapshot;
import views.GenericTextFileView;

/**
 * An implementation of the InvertedIndexControllerWithSnapshot for text files
 * @author JRRed
 *
 */
public class TextFileInvertedIndexControllerWithSnapshot implements InvertedIndexControllerWithSnapshot {
	
	private InvertedIndexWithSnapshot invertedIndex;
	
	private GenericTextFileView<InvertedIndex> view;
	
	public TextFileInvertedIndexControllerWithSnapshot(
			InvertedIndexWithSnapshot invertedIndex,
			GenericTextFileView<InvertedIndex> view) {
		this.invertedIndex = invertedIndex;
		this.view = view;
	}

	@Override
	public void tryDisplaying() {
		try {
			view.writeToFile();
		}
		catch (IOException e) {
			System.err.println("Error occurred in controller. Cancelling file write...");
			e.printStackTrace();
		}
		catch (Exception e) {
			System.err.println("This should never run");
			assert false;
		}
	}

	@Override
	public RowSortedTable<String, String, TreeSet<Integer>> snapshot() {
		return invertedIndex.snapshot();
	}
}
