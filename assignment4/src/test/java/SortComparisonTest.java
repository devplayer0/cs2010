import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class SortComparisonTest {
    @Test
    public void testConstructor() {
        new SortComparison();
    }

    @Test
    public void testEmpty() {
        double[] empty = new double[0];
        assertArrayEquals(empty, SortComparison.insertionSort(empty), 0);
        assertArrayEquals(empty, SortComparison.quickSort(empty), 0);
        assertArrayEquals(empty, SortComparison.mergeSortIterative(empty), 0);
        assertArrayEquals(empty, SortComparison.mergeSortRecursive(empty), 0);
        assertArrayEquals(empty, SortComparison.selectionSort(empty), 0);
    }

    @Test
    public void testNull() {
        assertNull(SortComparison.insertionSort(null));
        assertNull(SortComparison.quickSort(null));
        assertNull(SortComparison.mergeSortIterative(null));
        assertNull(SortComparison.mergeSortRecursive(null));
        assertNull(SortComparison.selectionSort(null));
    }

    @Test
    public void testSimple() {
        double[] simple = { 700, 150.5, 5, 200, 300, 170, 1337, 400, 2 };
        double[] simpleSorted = { 2, 5, 150.5, 170, 200, 300, 400, 700, 1337 };

        assertArrayEquals(simpleSorted, SortComparison.insertionSort(simple), 0);
        assertArrayEquals(simpleSorted, SortComparison.quickSort(simple), 0);
        assertArrayEquals(simpleSorted, SortComparison.mergeSortIterative(simple), 0);
        assertArrayEquals(simpleSorted, SortComparison.mergeSortRecursive(simple), 0);
        assertArrayEquals(simpleSorted, SortComparison.selectionSort(simple), 0);
    }
}
