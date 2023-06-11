package text_finding;

import java.io.IOException;

import text_stemming.TextFileStemmer;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.TreeSet;
import java.util.TreeMap;

/** Finds a collection of files (as paths) for the TextStemmer */
public class TextFileFinder implements TextSourceFinder<Path> {
	
	private Path dataSource;
	
	public TextFileFinder(Path dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * A lambda function that returns true if the path is a file that ends in a
	 * .txt or .text extension (case-insensitive). Useful for
	 * {@link Files#walk(Path, FileVisitOption...)}.
	 *
	 * @see Files#isRegularFile(Path, java.nio.file.LinkOption...)
	 * @see Path#getFileName()
	 * @see Files#walk(Path, FileVisitOption...)
	 */
	public static final Predicate<Path> IS_TEXT = p -> {
		if (!Files.isRegularFile(p)) {
			return false;
		}
		String fileName = p.getFileName().toString().toLowerCase();
		return fileName.endsWith(".txt")
				|| fileName.endsWith(".text");
	};
	
	@Override
	public Collection<Path> getTextSources() throws NullPointerException {
		if (Files.isDirectory(dataSource)) { // if the source is a directory, return all valid files
			return getSourcesFromDirectory();
		}
		else if (Files.isRegularFile(dataSource, java.nio.file.LinkOption.NOFOLLOW_LINKS)) { // if the source is a regular file, just return the file in its own list
			return List.of(dataSource);
		}
		return null;
	}
	
	/**
	 * Given a directory as a data source, returns all valid files in that directory
	 * @return all valid files in the given directory
	 */
	private Collection<Path> getSourcesFromDirectory() {
		try {
			return Files.walk(dataSource,  FileVisitOption.FOLLOW_LINKS)
					.filter(IS_TEXT)
					.collect(Collectors.toList());
		}
		catch (IOException e) {
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
