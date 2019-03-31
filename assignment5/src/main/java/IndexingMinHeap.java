import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class IndexingMinHeap<E extends Comparable<E>> extends MinHeap<E> {
    Map<E, Integer> indices;
    public IndexingMinHeap() {
        indices = new HashMap<>();
    }
    public IndexingMinHeap(int initialCapacity) {
        super(initialCapacity);
        indices = new HashMap<>(initialCapacity);
    }

    @Override
    protected void swap(int a, int b) {
        indices.put(heap.get(a), b);
        indices.put(heap.get(b), a);
        super.swap(a, b);

    }
    @Override
    public boolean add(E e) {
        if (indices.containsKey(e)) {
            return false;
        }

        indices.put(e, heap.size());
        return super.add(e);
    }
    @Override
    public E extractMin() {
        if (heap.size() == 0) {
            return null;
        }

        E min = getMin();

        E end = heap.get(heap.size()-1);
        heap.set(0, end);
        indices.put(end, 0);

        heap.remove(heap.size()-1);
        sinkDown(0);

        indices.remove(min);
        for (int i = 0; i < heap.size(); i++) {
            E e = heap.get(i);
            int index = indexOf(e);
            if (index != i) {
                System.err.printf("index of %s corrupted (should be %d, was %d)!\n", e, i, indexOf(e));
            }
        }
        return min;
    }
    public int indexOf(E e) {
        return indices.containsKey(e) ? indices.get(e) : -1;
    }
    @Override
    public boolean contains(Object o) {
        return indices.containsKey(o);
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        return indices.keySet().containsAll(c);
    }

    @Override
    public void clear() {
        super.clear();
        indices.clear();
    }
}
