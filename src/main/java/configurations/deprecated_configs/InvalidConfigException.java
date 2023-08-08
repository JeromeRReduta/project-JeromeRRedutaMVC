package configurations.deprecated_configs;

/**
 * Thrown when a given Config has values that should not be allowed.
 * What is/isn't allowed is defined by the Config implmentation/author.
 * @deprecated modular only now
 * @author JRRed
 *
 */
public class InvalidConfigException extends RuntimeException {

	/**
	 * Unused
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidConfigException(String message) {
		super(message);
	}
}
