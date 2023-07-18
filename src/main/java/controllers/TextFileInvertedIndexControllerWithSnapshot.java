package controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;

import com.google.common.collect.RowSortedTable;

import data.AbstractStemFileNameValueTable;
import views.DataToTextFileView;

public class TextFileInvertedIndexControllerWithSnapshot implements InvertedIndexControllerWithSnapshot {
	
	private AbstractStemFileNameValueTable<TreeSet<Integer>> invertedIndex;
	
	private DataToTextFileView view;
	
	public TextFileInvertedIndexControllerWithSnapshot(
		AbstractStemFileNameValueTable<TreeSet<Integer>> invertedIndex,
		DataToTextFileView view) {
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
		// TODO Auto-generated method stub
		return invertedIndex.snapshot();
	}
}
