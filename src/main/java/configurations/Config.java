package configurations;

import json.JsonWriteable;

/**
 * Allows project configs to be used all under
 * one supertype. No other use.
 * 
 * @author JRRed
 *
 */
public interface Config extends JsonWriteable {
	
	/**
	 * Factory pattern for a given Config
	 * @author JRRed
	 *
	 */
	public interface Factory<C extends Config> {
		
		/**
		 * Creates a new config from commandline args, validates it, and returns it if valid.
		 * Implementation should handle commandline arg processing
		 * @param validator A ConfigValidator
		 * @return a valid config
		 * @throws InvalidConfigException if the config is invalid, which should end the program
		 */
		default C createValidatedConfig() throws InvalidConfigException {
			C config = createConfig();
			getValidator(config).validate();
			return config;
		}
		
		/**
		 * Creates a config. This should be the ONLY WAY to get a new config
		 * @return
		 */
		C createConfig();
		
		/**
		 * Gets a validator for the Config
		 * @param Config config
		 * @return a validator for this Config
		 */
		ConfigValidator<C> getValidator(C Config);
	}
}
