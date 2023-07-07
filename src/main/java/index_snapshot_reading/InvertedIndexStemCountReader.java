package index_snapshot_reading;

import data.InvertedIndex;
import data.InvertedIndexWithSnapshot;
import data.StemCountInvertedIndex;

public class InvertedIndexStemCountReader implements StemCountReader {
	
	private StemCountInvertedIndex stemCountInvertedIndex;
	
	public InvertedIndexStemCountReader(StemCountInvertedIndex stemCountInvertedIndex) {
		this.stemCountInvertedIndex = stemCountInvertedIndex;
	}

	@Override
	public void readIntoMatchDataInvertedIndex(InvertedIndexWithSnapshot snapshot) {
		// TODO: Problem - can't actually access the map underneath the invertedIndex
		
		
	}

}
