package controllers;

import java.util.Collection;
import java.util.Map;

import data.InvertedIndex;
import views.DataToTextFileView;
import views.InvertedIndexView;

/**
 * Implementation of InvertedIndexController that writes its output to a file
 * @author JRRed
 *
 */
public class TextFileInvertedIndexController implements InvertedIndexController {

	private InvertedIndex model;
	
	private DataToTextFileView view;
	
	public TextFileInvertedIndexController(
			InvertedIndex model,
			DataToTextFileView view) {
		this.model = model;
		this.view = view;
	}
	
	@Override
	public void displayIndex() {
		view.writeToFile();
	}

	@Override
	public Map<String, ? extends Collection<Integer>> get(String stem) {
		return model.get(stem);
	}

}
