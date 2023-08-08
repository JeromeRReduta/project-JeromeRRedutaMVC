package configurations.modular_configs;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import argument_parsing.ArgumentMap;
import argument_parsing.CommandLineReader;
import data.InvertedIndex;
import data.InvertedIndexTable;
import data.stem_counting.StemCounter;
import data.stem_counting.StemCounterTable;
import data.search_results.SearchResultIndex;
import data.search_results.SearchResultIndexMap;

public class ModularProject2Config implements ModularConfig {
	
	public final InvertedIndexWithSnapshotConfig forInvertedIndex;
	
	public final StemCounterConfig forStemCounter;
	
	public final SearchResultIndexConfig forSearchResults;
	
	private ModularProject2Config(
			InvertedIndexWithSnapshotConfig forInvertedIndex,
			StemCounterConfig forStemCounter,
			SearchResultIndexConfig forSearchResults) {
		this.forInvertedIndex = forInvertedIndex;
		this.forStemCounter = forStemCounter;
		this.forSearchResults = forSearchResults;
	}

	@Override
	public void writeToJson(int baseIndent, Writer writer) throws IOException {
		ModularConfig.writeConfigsToJson(baseIndent, writer, List.of(
				forInvertedIndex,
				forStemCounter,
				forSearchResults));
	}
	
	@Override
	public String toString() {
		return toJsonString();
	}
	
	public static class Factory implements ModularConfig.Factory<ModularProject2Config> {

		private CommandLineReader reader;
		
		public Factory(String[] args) {
			this.reader = new ArgumentMap(args);
		}
		@Override
		public ModularProject2Config createConfig() {
			InvertedIndexWithSnapshotConfig forInvertedIndex
				= new InvertedIndexWithSnapshotConfig.Factory(reader)
					.createConfig();
			StemCounterConfig forStemCounter
				= new StemCounterConfig.Factory(reader, forInvertedIndex.model)
					.createConfig();
			SearchResultIndexConfig forSearchResults
				= new SearchResultIndexConfig.Factory(reader, forStemCounter.model)
					.createConfig();
			return new ModularProject2Config(
					forInvertedIndex,
					forStemCounter,
					forSearchResults);
		}
	}
}
