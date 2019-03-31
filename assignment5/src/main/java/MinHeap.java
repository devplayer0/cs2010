import java.util.*;

public class MinHeap<E extends Comparable<E>> implements Collection<E>, Queue<E> {
    List<E> heap;

    public MinHeap() {
        heap = new ArrayList<>();
    }
    public MinHeap(int initialCapacity) {
        heap = new ArrayList<>(initialCapacity);
    }

    @Override
    public int size() {
        return heap.size();
    }
    @Override
    public boolean isEmpty() {
        return heap.isEmpty();
    }
    @Override
    public boolean contains(Object o) {
        return heap.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        // Not ordered!
        return heap.iterator();
    }

    @Override
    public Object[] toArray() {
        return heap.toArray();
    }
    @Override
    public <T> T[] toArray(T[] a) {
        return heap.toArray(a);
    }

    static int pi(int i) {
        return (i - 1) / 2;
    }
    static int li(int i) {
        return (i * 2) + 1;
    }
    static int ri(int i) {
        return (i * 2) + 2;
    }
    protected void swap(int a, int b) {
        E tmp = heap.get(a);
        heap.set(a, heap.get(b));
        heap.set(b, tmp);
    }
    protected void bubbleUp(int i) {
        E e = heap.get(i);
        for (; i > 0 && e.compareTo(heap.get(pi(i))) < 0; i = pi(i)) {
            swap(i, pi(i));
        }
    }
    @Override
    public boolean add(E e) {
        heap.add(heap.size(), e);
        bubbleUp(heap.size()-1);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }
    @Override
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return extractMin();
    }
    @Override
    public E poll() {
        return extractMin();
    }
    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return getMin();
    }
    @Override
    public E peek() {
        return getMin();
    }

    public E getMin() {
        if (heap.size() == 0) {
            return null;
        }

        return heap.get(0);
    }
    protected void sinkDown(int i) {
        while (li(i) < heap.size()) {
            int j = li(i);
            if (j < heap.size()-1 && heap.get(li(i)).compareTo(heap.get(ri(i))) > 0) {
                j = ri(i);
            }
            if (heap.get(i).compareTo(heap.get(j)) <= 0) {
                break;
            }

            swap(i, j);
            i = j;
        }
    }
    public E extractMin() {
        if (heap.size() == 0) {
            return null;
        }

        E min = getMin();
        heap.set(0, heap.get(heap.size()-1));
        heap.remove(heap.size()-1);
        sinkDown(0);

        return min;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return heap.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return true;
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        heap.clear();
    }
}
