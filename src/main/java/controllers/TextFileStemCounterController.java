package controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import com.google.common.collect.RowSortedTable;

import data.stem_counting.StemCounter;
import views.GenericTextFileView;

public class TextFileStemCounterController
	implements StemCounterController {
	
	private StemCounter model;
	
	private GenericTextFileView<StemCounter> view;
	
	public TextFileStemCounterController(
			StemCounter model, GenericTextFileView<StemCounter> view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void tryDisplaying() {
		try {
			view.writeToFile();
		}
		catch (IOException e) {
			System.err.println("Error displaying stem counter; cancelling write...");
		}
		catch (Exception e) {
			System.err.println("TextFileStemCounterController - this should NEVER run");
			assert false;
		}
	}

	@Override
	public RowSortedTable<String, String, Integer> stemCountTableSnapshot() {
		return model.snapshotOfStemCountTable();
	}

	@Override
	public Map<String, Integer> totalStemsByFileNameSnapshot() {
		return model.snapshotOfStemCountsByFile();
	}
}
