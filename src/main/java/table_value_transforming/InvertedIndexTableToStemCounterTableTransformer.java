package table_value_transforming;

import java.util.TreeSet;

import data.AbstractStringKeyTable;
import data.InvertedIndexTable;
import data.stem_counting.StemCounterTable;

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
