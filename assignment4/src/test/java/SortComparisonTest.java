import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertArrayEquals;

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
    }

    @Test
    public void testSimple() {
        double[] simple = { 700, 150.5, 5, 200, 300, 170, 1337, 400, 2 };
        double[] simpleSorted = { 2, 5, 150.5, 170, 200, 300, 400, 700, 1337 };

        assertArrayEquals(simpleSorted, SortComparison.insertionSort(simple), 0);
    }
}
