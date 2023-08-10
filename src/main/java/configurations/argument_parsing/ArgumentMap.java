package configurations.argument_parsing;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.HashMap;

public class ArgumentMap
	extends HashMap<String, String>
	implements CommandLineReader {

	/**
	 * Unused
	 */
	private static final long serialVersionUID = 1L;
	
	public ArgumentMap(String[] args) {
		parse(args);
	}

	@Override
	public void parse(String[] args) {
		// Sanity checks - args isn't null or empty
		if (args == null) {
			throw new NullPointerException("args was null");
		}
		if (args.length == 0) {
			return;
		}
		for (int i = 0; i < args.length - 1; i++) {
			if (isValue(args[i])) {
				continue;
			}
			String key = args[i];
			String value = isValue(args[i + 1])
					? args[i + 1]
					: null;
			super.put(key, value);
		}
		// Tail case - last one is flag (if last str is value, do nothing)
		String last = args[args.length - 1];
		if (isFlag(last)) {
			super.put(last,  null);
		}
	}

	/**
	 * Determines whether arg is a flag. Arg is a flag if:
	 * (1) arg is non-null
	 * (2) arg is >= 2 chars
	 * (3) arg[0] is a dash, and arg[1] is any letter
	 * @param arg commandline argument
	 * @return whether arg is a flag
	 */
	public static boolean isFlag(String arg) {
		if (arg == null) {
			return false;
		}
		if (arg.length() < 2) {
			return false;
		}
		if (!arg.startsWith("-")) {
			return false;
		}
		return Character.isLetter(
				arg.charAt(1));
	}
	
	/**
	 * Determines whether arg is a value, aka not a flag
	 * @param arg commandline argument
	 * @return whether arg is a value
	 */
	public static boolean isValue(String arg) {
		return !isFlag(arg);
	}

	@Override
	public String getString(String flag, String defaultValue) {
		String value = super.get(flag);
		return value != null ? value : defaultValue;
	}

	@Override
	public Path getPath(String flag, Path defaultValue) {
		String fileName = super.get(flag);
		try {
			return Path.of(fileName);
		}
		catch (InvalidPathException | NullPointerException e) { // Path cannot resolve or pathStr doesn't exist/is null
			return defaultValue;
		}
		catch (Exception e) { // this should not run
			e.printStackTrace();
			return defaultValue;
		}
	}

	@Override
	public int getInteger(String flag, int defaultValue) {
		String value = super.get(flag);
		try {
			return Integer.parseInt(value);
		}
		catch (NumberFormatException | NullPointerException e) {
			return defaultValue;
		}
		catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}
	
	@Override
	public boolean hasNonNullValue(String flag) {
		return super.get(flag) != null;
	}
	
	@Override
	public boolean containsFlag(String flag) {
		return super.containsKey(flag);
	}
}
