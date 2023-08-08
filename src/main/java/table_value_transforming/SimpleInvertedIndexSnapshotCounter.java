package table_value_transforming;

import data.InvertedIndexWithSnapshot;
import data.stem_counting.StemCounter;

public class SimpleInvertedIndexSnapshotCounter 
	implements InvertedIndexSnapshotCounter {
	
	private final InvertedIndexWithSnapshot index;
	
	private final StemCounter counter;
	
	public SimpleInvertedIndexSnapshotCounter(
			InvertedIndexWithSnapshot index,
			StemCounter counter) {
		this.index = index;
		this.counter = counter;
	}

	@Override
	public void countStems() {
		index.snapshot().cellSet().forEach(cell ->
			counter.add(
					cell.getRowKey(),
					cell.getColumnKey(),
					cell.getValue().size()));
		}
}
