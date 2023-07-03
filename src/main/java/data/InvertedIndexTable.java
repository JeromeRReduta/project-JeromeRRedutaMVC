package data;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import json.JsonCollectionWriter;
import json.JsonTableWriter;


/* TODO:
 * (x) 1. Formatting + documentation for:
 * 	InvIndexTable
 *	JsonTableWriter
 *
 *	(x) 1.5. Deprecate all other forms of InvertedIndex not based on tables
 *
 *	(x) 2. Organize import
 *
 *	3. See if you can rename this to Project 1.5 - Project 1 w/ Tables
 *
 *	4. Make CRC cards
 *
 *	5. Add CRC cards to README
 */

/**
 * Table-based implementation of an InvertedIndex, backed by Guava's TreeBasedTable.
 * @author JRRed
 *
 */
public class InvertedIndexTable implements InvertedIndex {
	
	private Table<String, String, TreeSet<Integer>> backingTable;
	/** Defines how this class writes its values. In this case, b/c our value is
	 * 	a treeset of ints, we use JsonCollectionWriter.writeCollection()
	 */
	private JsonTableWriter<String, String, TreeSet<Integer>> tableWriter;
	
	public InvertedIndexTable() {
		backingTable = TreeBasedTable.create();
		tableWriter = JsonCollectionWriter::writeCollection;
	}

	@Override
	public void writeToJson(int baseIndent, Writer writer) throws IOException {
		tableWriter.writeAllElements(baseIndent, writer, backingTable);
	}

	@Override
	public void add(String stem, String fileName, int position) {
		if (backingTable.get(stem, fileName) == null) {
			backingTable.put(stem, fileName, new TreeSet<>());
		}
		backingTable.get(stem, fileName).add(position);
	}

	@Override
	public Map<String, ? extends Collection<Integer>> get(String stem) {
		return backingTable.row(stem);
	}
	
	@Override
	public String toString() {
		return toJsonString();
	}
}
