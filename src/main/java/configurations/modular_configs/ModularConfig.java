package configurations.modular_configs;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

import json.JsonCollectionWriter;
import json.JsonWriteable;

/**
 * A more modular implementation for configs.
 * 
 * @author JRRed
 *
 */
public interface ModularConfig extends JsonWriteable {
	
	default String getClassString(Object object) {
		return object == null ? "(null)" : object.getClass().getSimpleName();
	}
	
	static void writeConfigsToJson(int baseIndent, Writer writer, Collection<ModularConfig> configs) throws IOException {
		JsonCollectionWriter.writeJsonWriteableCollection(baseIndent, writer, configs);
	}
	
	/**
	 * Factory pattern for a given Config
	 * @author JRRed
	 *
	 */
	public interface Factory<C extends ModularConfig> {
		
		/**
		 * Creates a config. This should be the ONLY WAY to get a new config
		 * @return
		 */
		C createConfig();
	}
}
