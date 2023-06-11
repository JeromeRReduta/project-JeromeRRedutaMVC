package configurations;

import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Project 1 implementation of ConfigValidator
 * @author JRRed
 *
 */
public class Project1ConfigValidator implements ConfigValidator<Project1Config> {
	
	private final Map<String, Object> crucialDependencies;
	
	private final List<String> invalidDependencyNames;
	
	public Project1ConfigValidator(Project1Config config) {
		this.crucialDependencies = new HashMap<>();
		crucialDependencies.put("invertedIndex", config.invertedIndex);
		crucialDependencies.put("invertedIndexView", config.invertedIndexView);
		crucialDependencies.put("invertedIndexController", config.invertedIndexController);
		crucialDependencies.put("textFinder", config.textFinder);
		crucialDependencies.put("textStemmer", config.textStemmer);
		crucialDependencies.put("stemReader", config.stemReader);
		this.invalidDependencyNames = new LinkedList<>();
	}

	@Override
	public void validate() throws InvalidConfigException {
		crucialDependencies.entrySet().stream()
			.filter(entry -> entry.getValue() == null)
			.forEach(entry -> {
				String nameOfNullDependency = entry.getKey();
				invalidDependencyNames.add(nameOfNullDependency);
				});
		if (!invalidDependencyNames.isEmpty()) {
			String message = "The following dependencies are null but shouldn't be: " + invalidDependencyNames
					+ "\nTerminating program...";
			System.err.println(message);
			throw new InvalidConfigException(message);
		}
	}
}
