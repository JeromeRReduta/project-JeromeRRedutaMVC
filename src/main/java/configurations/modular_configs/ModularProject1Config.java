package configurations.modular_configs;

import java.io.IOException;
import java.io.Writer;

import configurations.argument_parsing.ArgumentMap;
import configurations.argument_parsing.CommandLineReader;

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
