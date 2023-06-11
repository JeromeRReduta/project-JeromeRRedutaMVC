package configurations;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Project1ConfigValidator implements ConfigValidator<Project1Config> {
	
	private Map<String, Object> crucialDependencies;
	
	private List<String> invalidDependencyNames;
	
	public Project1ConfigValidator(Project1Config config) {
		this.crucialDependencies = Map.of(
				"invertedIndex", config.invertedIndex,
				"invertedIndexView", config.invertedIndexView,
				"invertedIndexController", config.invertedIndexController,
				"textFinder", config.textFinder,
				"textStemmer", config.textStemmer,
				"stemReader", config.stemReader);
		this.invalidDependencyNames = new LinkedList<>();
	}

	@Override
	public void validate() throws InvalidConfigException {
		crucialDependencies.forEach((name, object) -> {
			if (object == null) {
				invalidDependencyNames.add(name);
			}
		});
		if (!invalidDependencyNames.isEmpty()) {
			throw new InvalidConfigException("The following dependencies are null but shouldn't be: " + invalidDependencyNames);
		}
	}
}
