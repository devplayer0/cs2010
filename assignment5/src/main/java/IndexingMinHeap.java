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
        E min = super.extractMin();
        if (min == null) {
            return null;
        }

        indices.remove(min);
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
