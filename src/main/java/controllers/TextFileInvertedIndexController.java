package controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import data.InvertedIndex;
import views.DataToTextFileView;

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
	public void tryDisplayingIndex() {
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
	public Map<String, ? extends Collection<Integer>> get(String stem) {
		return model.get(stem);
	}

}
