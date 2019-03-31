import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PriorityQueue<E, P extends Comparable<P>> {
    private IndexingMinHeap<QueueItem> heap;
    private Map<E, QueueItem> items;
    class QueueItem implements Comparable<QueueItem> {
        private E e;
        private P p;
        public QueueItem(E e, P p) {
            this.e = e;
            this.p = p;
        }

        @Override
        public int compareTo(QueueItem o) {
            return p.compareTo(o.p);
        }

        @Override
        public int hashCode() {
            return Objects.hash(e, p);
        }
        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (o.getClass() != getClass()) {
                return false;
            }

            QueueItem item = (QueueItem)o;
            return Objects.equals(item.e, e) && Objects.equals(item.p, p);
        }
    }

    public PriorityQueue() {
        heap = new IndexingMinHeap<>();
        items = new HashMap<>();
    }
    public PriorityQueue(int initialCapacity) {
        heap = new IndexingMinHeap<>(initialCapacity);
        items = new HashMap<>(initialCapacity);
    }

    public boolean insert(E e, P p) {
        if (items.containsKey(e)) {
            return false;
        }

        QueueItem i = new QueueItem(e, p);
        heap.add(i);
        items.put(e, i);
        return true;
    }
    public void insertAll(Map<E, P> eMap) {
        for (Map.Entry<E, P> e : eMap.entrySet()) {
            insert(e.getKey(), e.getValue());
        }
    }

    public E getMin() {
        if (heap.isEmpty()) {
            return null;
        }

        return heap.getMin().e;
    }
    public E extractMin() {
        if (heap.isEmpty()) {
            return null;
        }

        return heap.extractMin().e;
    }
    public P getPriority(E e) {
        if (!items.containsKey(e)) {
            return null;
        }

        return items.get(e).p;
    }
    public boolean changePriority(E e, P newP) {
        if (!items.containsKey(e)) {
            insert(e, newP);
            return true;
        }

        QueueItem item = items.get(e);
        int index = heap.indices.get(item);
        int cmp = newP.compareTo(item.p);
        item.p = newP;
        if (cmp < 0) {
            heap.bubbleUp(index);
        } else if (cmp > 0) {
            heap.sinkDown(index);
        } else {
            return false;
        }

        return true;
    }

    public void clear() {
        heap.clear();
        items.clear();
    }
    public boolean isEmpty() {
        return items.isEmpty();
    }
    public int size() {
        return items.size();
    }
}
