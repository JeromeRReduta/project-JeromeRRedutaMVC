package configurations.modular_configs;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Map;

import configurations.argument_parsing.CommandLineReader;
import controllers.stem_counting.StemCounterController;
import controllers.stem_counting.TextFileStemCounterController;
import data_reading.stem_counting.InvertedIndexSnapshotCounter;
import data_reading.stem_counting.SimpleInvertedIndexSnapshotCounter;
import json.JsonMapWriter;
import models.stem_counting.StemCounter;
import models.stem_counting.StemCounterTable;
import models.stem_indexing.InvertedIndexWithSnapshot;
import views.DataToTextFileView;
import views.GenericTextFileView;

/**
 * Modular config for the StemCounter MVC and data reading
 * @author JRRed
 *
 */
public class StemCounterConfig implements ModularConfig {
	
	/** Flags and outputs as decided by project requirements */
	private final static String OUTPUT_FLAG = "-counts";
	
	private final static Path DEFAULT_OUTPUT = Path.of("counts.json");
	
	/** Config vars */
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
	
	/**
	 * Factory pattern
	 * @author JRRed
	 *
	 */
	public static class Factory implements ModularConfig.Factory<StemCounterConfig> {

		private final CommandLineReader reader;
		
		private final InvertedIndexWithSnapshot invertedIndex;
		
		public Factory(CommandLineReader reader, InvertedIndexWithSnapshot invertedIndex) {
			this.reader = reader;
			this.invertedIndex = invertedIndex;
		}
		
		@Override
		public StemCounterConfig createConfig() {
			StemCounter model = new StemCounterTable();
			Path outputFile = reader.getPath(OUTPUT_FLAG, DEFAULT_OUTPUT);
			GenericTextFileView<StemCounter> view = new GenericTextFileView<>(model, outputFile);
			StemCounterController controller = new TextFileStemCounterController(model, view);
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
