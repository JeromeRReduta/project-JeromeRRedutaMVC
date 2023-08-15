package configurations.modular_configs;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import json.JsonCollectionWriter;
import json.JsonWriteable;

/**
 * A more modular implementation for configs. This allows for a modular config to use other modular
 * configs as vars, making this fit the composite pattern.
 * 
 * @author JRRed
 *
 */
public interface ModularConfig extends JsonWriteable {
	
	/**
	 * Utiity method for getting a simple class name for a given config's attribute
	 * @param object a given config's attribute
	 * @return the simple class name of the given attribute
	 */
	default String getClassString(Object object) {
		return object == null ? "(null)" : object.getClass().getSimpleName();
	}
	
	/**
	 * Utility method for writing a collection of configs. Useful for a high-level config whose
	 * attributes are other configs
	 * @param baseIndent
	 * @param writer
	 * @param configs
	 * @throws IOException
	 */
	static void writeConfigsToJson(int baseIndent, Writer writer, Collection<ModularConfig> configs)
			throws IOException {
		JsonCollectionWriter.writeJsonWriteableCollection(baseIndent, writer, configs);
	}
	
	/**
	 * Factory pattern for a given ModularConfig
	 * @author JRRed
	 *
	 */
	public interface Factory<C extends ModularConfig> {
		
		/**
		 * Creates a config. This should be the ONLY WAY to get a new config
		 * @return a modular config implementation
		 */
		C createConfig();
	}
}
