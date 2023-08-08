package configurations.modular_configs;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import argument_parsing.ArgumentMap;
import argument_parsing.CommandLineReader;
import controllers.InvertedIndexController;
import data.InvertedIndex;
import data.InvertedIndexTable;
import json.JsonCollectionWriter;
import stem_reading.StemReader;
import stem_reading.text_finding.TextSourceFinder;
import stem_reading.text_stemming.TextFileStemmer;
import stem_reading.text_stemming.TextStemmer;
import views.GenericTextFileView;

/**
 * Project 1 Implementation of Config
 * @author JRRed
 *
 */
public class ModularProject1Config implements ModularConfig {
	
	private InvertedIndexWithSnapshotConfig invertedIndex;
	
	private ModularProject1Config(InvertedIndexWithSnapshotConfig invertedIndex) {
		this.invertedIndex = invertedIndex;
	}
	
	@Override
	public void writeToJson(int baseIndent, Writer writer) throws IOException {
		invertedIndex.writeToJson(baseIndent, writer);
	}
	
	public String toString() {
		return toJsonString();
	}
	
	public static class Factory implements ModularConfig.Factory<ModularProject1Config> {

		private CommandLineReader reader;
		
		public Factory(String[] args) {
			this.reader = new ArgumentMap(args);
		}
		
		@Override
		public ModularProject1Config createConfig() {
			return new ModularProject1Config(
					new InvertedIndexWithSnapshotConfig.Factory(reader).createConfig());
		}
	}
}
