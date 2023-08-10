package configurations.modular_configs;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import configurations.argument_parsing.ArgumentMap;
import configurations.argument_parsing.CommandLineReader;
import controllers.stem_indexing.InvertedIndexController;
import data.stem_indexing.InvertedIndex;
import data.stem_indexing.InvertedIndexTable;
import data_reading.stem_indexing.StemReader;
import data_reading.stem_indexing.TextFileStemmer;
import data_reading.stem_indexing.TextSourceFinder;
import data_reading.stem_indexing.TextStemmer;
import json.JsonCollectionWriter;
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
