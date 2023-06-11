package configurations;

/**
 * Validates a given project Config based on some rules. These rules are up to the implementation/author.
 * @author JRRed
 *
 * @param <C> Type of Config
 */
public interface ConfigValidator<C extends Config> {
	
	/**
	 * Validates the Config. Throws an exception if invalid.
	 * @implNote When implementing this function, please comment the implementation's definition of "valid"
	 * @throws InvalidConfigException If config is invalid.
	 */
	void validate() throws InvalidConfigException;
}
