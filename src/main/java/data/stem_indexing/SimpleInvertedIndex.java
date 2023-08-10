package data.stem_indexing;

import java.io.IOException;
import java.io.Writer;
import java.util.TreeMap;
import java.util.TreeSet;

import json.JsonCollectionWriter;
import json.JsonMapWriter;
import json.JsonWriteable;

public class SimpleInvertedIndex
	extends TreeMap<String, data.stem_indexing.SimpleInvertedIndex.FileNamePositionMap>
	implements InvertedIndex {

	/**
	 * Unused
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void add(String stem, String fileName, int position) {
		super.putIfAbsent(stem, new FileNamePositionMap());
		FileNamePositionMap innerMap = superGet(stem);
		innerMap.putIfAbsent(fileName, new StemPositionSet());
		StemPositionSet positions = innerMap.superGet(fileName);
		positions.add(position);
	}
	
	@Override
	public FileNamePositionMap get(String stem) {
		return super.get(stem).clone();
	}
	
	/**
	 * Lets the index call the actual get method of its inner data struct
	 * @param stem stem
	 * @return the actual FileNamePositionMap associated with the stem
	 */
	private FileNamePositionMap superGet(String stem) {
		return super.get(stem);
	}
	
	@Override
	public void writeToJson(int baseIndent, Writer writer) throws IOException {
		JsonMapWriter.writeJsonWriteableMap(baseIndent, writer, this);
	}
	
	/*  Note that the toString() of the outer data structures don't actually call any of the
	 *  toString()'s of the inner data structures. This is because we're using writeToJson() to
	 *  format the String, not toString() itself. The other toStrings() are mainly for printing/debugging
	 */
	@Override
	public String toString() {
		try {
			return toJsonString();
		}
		catch (Exception e) {
			System.err.println("Should never run");
			return null;
		}
	}
	
	/**
	 * Creates a cloned InvertedIndex, then adds cloned FileNamePositionMaps 
	 * to the cloned index, then returns index
	 */
	@Override
	public SimpleInvertedIndex clone() {
		SimpleInvertedIndex clonedIndex = new SimpleInvertedIndex();
		super.entrySet().forEach(entry -> clonedIndex.put(
				entry.getKey(),
				entry.getValue().clone()));
		return clonedIndex;
	}
	
	/** Implementation of the inner map, holding FileName-StemPositionSet pairs */
	public class FileNamePositionMap
		extends TreeMap<String, data.stem_indexing.SimpleInvertedIndex.StemPositionSet>
		implements JsonWriteable, Cloneable {
		
		/**
		 * Unused
		 */
		private static final long serialVersionUID = 1L;

		private FileNamePositionMap() {}
		
		/**
		 * Returns a CLONE of the innermost data structure, the StemPositionSet
		 * @param fileName fileName, used as a key
		 * @return a CLONE of the StemPositionSet associated with this key
		 */
		public StemPositionSet get(String fileName) {
			return super.get(fileName).clone();
		}
		
		/**
		 * Lets the map get the actual inner set
		 * @param fileName fileName
		 * @return inner set associated with this fileName
		 */
		private StemPositionSet superGet(String fileName) {
			return super.get(fileName);
		}
		
		@Override
		public FileNamePositionMap clone() {
			FileNamePositionMap clonedMap = new FileNamePositionMap();
			super.entrySet().forEach(entry -> clonedMap.put(
					entry.getKey(),
					entry.getValue().clone()));
			return clonedMap;
		}
		
		@Override
		public void writeToJson(int baseIndent, Writer writer) throws IOException {
			JsonMapWriter.writeJsonWriteableMap(baseIndent, writer, this);
		}
		
		@Override
		public String toString() {
			try {
				return toJsonString();
			}
			catch (Exception e) {
				System.err.println("Should never run");
				return null;
			}
		}
	}
	
	/** Implementation of the collection of file positions where a given stem is found in a given file
	 * 
	 * @author JRRed
	 *
	 */
	public class StemPositionSet
		extends TreeSet<Integer>
		implements JsonWriteable, Cloneable {
		
		/**
		 * Unused
		 */
		private static final long serialVersionUID = 1L;

		private StemPositionSet() {}
		
		@Override
		public StemPositionSet clone() {
			return (StemPositionSet)super.clone();
		}
	
		@Override
		public void writeToJson(int baseIndent, Writer writer) throws IOException {
			JsonCollectionWriter.writeCollection(baseIndent, writer, this);
		}
		
		@Override
		public String toString() {
			try {
				return toJsonString();
			}
			catch (Exception e) {
				System.err.println("Should never run");
				return null;
			}
		}
	}
}
