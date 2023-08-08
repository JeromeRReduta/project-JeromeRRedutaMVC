package configurations.modular_configs;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeSet;

import argument_parsing.CommandLineReader;
import controllers.StemCounterController;
import controllers.TextFileStemCounterController;
import data.InvertedIndex;
import data.InvertedIndexWithSnapshot;
import data.stem_counting.StemCounter;
import data.stem_counting.StemCounterTable;
import json.JsonMapWriter;
import table_value_transforming.InvertedIndexSnapshotCounter;
import table_value_transforming.SimpleInvertedIndexSnapshotCounter;
import table_value_transforming.StringKeyTableValueTransformer;
import views.DataToTextFileView;
import views.GenericTextFileView;

public class StemCounterConfig implements ModularConfig {
	
	public final InvertedIndexWithSnapshot invertedIndex;
	
	public final InvertedIndexSnapshotCounter invertedIndexSnapshotCounter;
	
	public final StemCounter model;
	
	public final DataToTextFileView view;
	
	public final StemCounterController controller;
	
	public final Path outputFile;
	
	private StemCounterConfig(
			InvertedIndexWithSnapshot invertedIndex,
			InvertedIndexSnapshotCounter invertedIndexSnapshotCounter,
			StemCounter model,
			DataToTextFileView view,
			StemCounterController controller,
			Path outputFile) {
		this.invertedIndex = invertedIndex;
		this.invertedIndexSnapshotCounter = invertedIndexSnapshotCounter;
		this.model = model;
		this.view = view;
		this.controller = controller;
		this.outputFile = outputFile;
		
	}
	
	@Override
	public void writeToJson(int baseIndent, Writer writer) throws IOException {
		Map<String, String> configAsMap = Map.of(
				"Inverted Index", getClassString(invertedIndex),
				"Transformer", getClassString(invertedIndexSnapshotCounter),
				"StemCounter", getClassString(model),
				"View", getClassString(view),
				"Controller", getClassString(controller),
				"Output File", outputFile == null ? "(null)" : outputFile.toString());
		JsonMapWriter.writeMap(baseIndent, writer, configAsMap);
	}
	
	@Override
	public String toString() {
		return toJsonString();
	}
	
	public static class Factory implements ModularConfig.Factory<StemCounterConfig> {

		private final CommandLineReader reader;
		
		private final InvertedIndexWithSnapshot invertedIndex;
		
		public Factory(
				CommandLineReader reader, InvertedIndexWithSnapshot invertedIndex) {
			this.reader = reader;
			this.invertedIndex = invertedIndex;
		}
		
		@Override
		public StemCounterConfig createConfig() {
			StemCounter model = new StemCounterTable();
			Path outputFile = reader.getPath("-counts", Path.of("counts.json")); // TODO: Make -counts and counts.json global static values
			GenericTextFileView<StemCounter> view
				= new GenericTextFileView<>(model, outputFile);
			StemCounterController controller
				= new TextFileStemCounterController(model, view);
			InvertedIndexSnapshotCounter invertedIndexSnapshotCounter
				= new SimpleInvertedIndexSnapshotCounter(invertedIndex, model);
			return new StemCounterConfig(
					invertedIndex,
					invertedIndexSnapshotCounter,
					model,
					view,
					controller,
					outputFile);
		}
	}
}
