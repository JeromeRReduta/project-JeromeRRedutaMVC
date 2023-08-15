package data_reading.stem_counting;

import models.stem_counting.StemCounter;
import models.stem_indexing.InvertedIndexWithSnapshot;

/**
 * Implementation of an InvertedIndexSnapshotCounter
 * @author JRRed
 *
 */
public class SimpleInvertedIndexSnapshotCounter implements InvertedIndexSnapshotCounter {
	
	private final InvertedIndexWithSnapshot index;
	
	private final StemCounter counter;
	
	public SimpleInvertedIndexSnapshotCounter(InvertedIndexWithSnapshot index, StemCounter counter) {
		this.index = index;
		this.counter = counter;
	}

	@Override
	public void countStems() {
		index.snapshot().cellSet().forEach(cell -> counter.add(
					cell.getRowKey(),
					cell.getColumnKey(),
					cell.getValue().size()));
		}
}
