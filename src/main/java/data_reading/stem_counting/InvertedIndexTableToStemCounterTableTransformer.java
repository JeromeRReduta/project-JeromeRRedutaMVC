package data_reading.stem_counting;

import java.util.TreeSet;

import data.AbstractStringKeyTable;
import data.stem_counting.StemCounterTable;
import data.stem_indexing.InvertedIndexTable;

public class InvertedIndexTableToStemCounterTableTransformer
	implements StringKeyTableValueTransformer<TreeSet<Integer>, Integer> {

	private final InvertedIndexTable index;
	
	private final StemCounterTable counter;
	
	public InvertedIndexTableToStemCounterTableTransformer(
			InvertedIndexTable index, StemCounterTable counter) {
		this.index = index;
		this.counter = counter;
	}
	
	public void transform() {
		transform(index, counter);
	}
	@Override
	public Integer transformValue(TreeSet<Integer> value) {
		return value.size();
	}
}
