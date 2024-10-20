package edu.neu.coe.info6205.pq;

import java.util.Comparator;

public class FourAryHeap<K> extends PriorityQueue<K>{

    public FourAryHeap(int n, Comparator<K> comparator, boolean floyd) {
        super(n, 1, true, comparator, floyd);
    }

    @Override
    protected int parent(int k) {
        return (k - getFirst() - 1) / 4 + getFirst();
    }

    @Override
    protected int firstChild(int k) {
        return 4 * (k - getFirst()) + 1 + getFirst();
    }
}
