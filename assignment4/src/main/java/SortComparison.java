// -------------------------------------------------------------------------

/**
 *  This class contains static methods that implementing sorting of an array of numbers
 *  using different sort algorithms.
 *
 *  @author
 *  @version HT 2019
 */

class SortComparison {
    private static void swap(double[] arr, int a, int b) {
        double tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }

    /**
     * Sorts an array of doubles using InsertionSort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     * @param toSort: An unsorted array of doubles.
     * @return array sorted in ascending order.
     *
     */
    static double[] insertionSort(double[] toSort) {
        if (toSort == null) {
            return null;
        }

        double[] a = toSort.clone();
        for (int i = 1; i < a.length; i++) {
            for (int j = i; j > 0 && a[j-1] > a[j]; j--) {
                swap(a, j, j - 1);
            }
        }

        return a;
    }

    private static int partition(double[] a, int lo, int hi) {
        // Median-of-three pivot selection
        int mid = (lo + hi) >>> 1;
        if (a[mid] < a[lo]) {
            swap(a, mid, lo);
        }
        if (a[hi] < a[lo]) {
            swap(a, hi, lo);
        }
        if (a[mid] < a[hi]) {
            swap(a, mid, hi);
        }

        double pivot = a[hi];
        int i = lo - 1;
        int j = hi + 1;
        while (true) {
            do {
                i++;
            } while (a[i] < pivot);
            do {
                j--;
            } while (a[j] > pivot);

            if (i >= j) {
                return j;
            }
            swap(a, i, j);
        }
    }
    private static void qsort(double[] a, int lo, int hi) {
        if (lo >= hi) {
            return;
        }

        // 3-pivot
        int pMid = partition(a, lo, hi);
        int pLo = partition(a, lo, pMid);
        int pHi = partition(a, pMid + 1, hi);

        qsort(a, lo, pLo);
        qsort(a, pLo + 1, pMid);
        qsort(a, pMid + 1, pHi);
        qsort(a, pHi + 1, hi);
    }

    /**
     * Sorts an array of doubles using Quick Sort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     * @param toSort: An unsorted array of doubles.
     * @return array sorted in ascending order
     *
     */
    static double[] quickSort(double[] toSort) {
        if (toSort == null) {
            return null;
        }

        double[] a = toSort.clone();
        qsort(a, 0, toSort.length - 1);
        return a;
    }

    private static void msortMerge(double[] a, double[] b, int lo, int mid, int hi) {
        int i = lo;
        int j = mid;
        for (int k = lo; k < hi; k++) {
            if (i < mid && (j >= hi || a[i] <= a[j])) {
                b[k] = a[i++];
            } else {
                b[k] = a[j++];
            }
        }
    }
    /**
     * Sorts an array of doubles using iterative implementation of Merge Sort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     *
     * @param toSort: An unsorted array of doubles.
     * @return after the method returns, the array must be in ascending sorted order.
     */
    static double[] mergeSortIterative(double toSort[]) {
        if (toSort == null) {
            return null;
        }

        double[] a = toSort.clone();
        double[] b = a.clone();
        for (int size = 1; size < a.length; size = 2*size) {
            for (int lo = 0; lo < a.length; lo += 2*size) {
                msortMerge(a, b, lo, Math.min(lo+size, a.length), Math.min(lo + (2*size), a.length));
            }

            double[] tmp = b;
            b = a;
            a = tmp;
        }

        return a;
    }

    private static void msort(double[] a, double[] b, int lo, int hi) {
        if (hi - lo < 2) {
            return;
        }

        int mid = (lo + hi) >>> 1;
        msort(b, a, lo, mid);
        msort(b, a, mid, hi);
        msortMerge(b, a, lo, mid, hi);
    }
    /**
     * Sorts an array of doubles using recursive implementation of Merge Sort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     *
     * @param toSort: An unsorted array of doubles.
     * @return after the method returns, the array must be in ascending sorted order.
     */
    static double[] mergeSortRecursive(double toSort[]) {
        if (toSort == null) {
            return null;
        }

        double[] a = toSort.clone();
        msort(a, a.clone(), 0, toSort.length);
        return a;
    }

    /**
     * Sorts an array of doubles using Selection Sort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     * @param toSort: An unsorted array of doubles.
     * @return array sorted in ascending order
     *
     */
    static double[] selectionSort(double toSort[]) {
        if (toSort == null) {
            return null;
        }

        double[] a = toSort.clone();
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[min]) {
                    min = j;
                }

            }

            if (min != i) {
                swap(a, i, min);
            }
        }

        return a;
    }
}
