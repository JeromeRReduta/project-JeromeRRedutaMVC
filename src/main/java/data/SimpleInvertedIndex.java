package data;

import java.io.IOException;
import java.io.Writer;
import java.util.TreeMap;
import java.util.TreeSet;

import json.JsonWriteable;

//TODO: FINISH IMPLEMENTATION

public class SimpleInvertedIndex
	extends TreeMap<String, data.SimpleInvertedIndex.FileNamePositionMap>
	implements InvertedIndex {

	/**
	 * Unused
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void add(String stem, String fileName, int position) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public FileNamePositionMap get(String stem) {
		return super.get(stem).clone();
	}
	

	@Override
	public void writeToJson(Writer writer, int baseIndent) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public SimpleInvertedIndex clone() {
		return null;
	}
	
	class FileNamePositionMap
		extends TreeMap<String, data.SimpleInvertedIndex.FileNamePositionMap.StemPositionList>
		implements JsonWriteable, Cloneable {
		
		/**
		 * Unused
		 */
		private static final long serialVersionUID = 1L;

		public FileNamePositionMap() {
			
		}
		
		
		public StemPositionList get(String fileName) {
			return super.get(fileName).clone();
		}

	
		
		
		@Override
		public FileNamePositionMap clone() {
			return null;
		}
		
		class StemPositionList
			extends TreeSet<Integer>
			implements JsonWriteable, Cloneable {


			
			@Override
			public StemPositionList clone() {
				return null;
			}
			
		}
		
	}

}
