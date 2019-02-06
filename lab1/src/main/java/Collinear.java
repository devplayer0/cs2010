/**
 * This class contains only two static methods that search for points on the
 * same line in three arrays of integers.
 *
 * @author Jack O'Sullivan
 * @version 18/09/18 9:15:15
 */
class Collinear {
	/**
	 * Counts for the number of non-hoizontal lines that go through 3 points in arrays a1, a2, a3.
	 * This method is static, thus it can be called as Collinear.countCollinear(a1,a2,a3)
	 *
	 * @param a1: An UNSORTED array of integers. Each integer a1[i] represents the point (a1[i], 1) on the plain.
	 * @param a2: An UNSORTED array of integers. Each integer a2[i] represents the point (a2[i], 2) on the plain.
	 * @param a3: An UNSORTED array of integers. Each integer a3[i] represents the point (a3[i], 3) on the plain.
	 * @return the number of points which are collinear and do not lie on a horizontal line.
	 * <p>
	 * Array a1, a2 and a3 contain points on the horizontal line y=1, y=2 and y=3, respectively.
	 * A non-horizontal line will have to cross all three of these lines. Thus
	 * we are looking for 3 points, each in a1, a2, a3 which lie on the same
	 * line.
	 * <p>
	 * Three points (x1, y1), (x2, y2), (x3, y3) are collinear (i.e., they are on the same line) if
	 * <p>
	 * x1*(y2-y3)+x2*(y3-y1)+x3*(y1-y2)=0
	 * <p>
	 * In our case y1=1, y2=2, y3=3.
	 * <p>
	 * You should implement this using a BRUTE FORCE approach (check all possible combinations of numbers from a1, a2, a3)
	 * <p>
	 * ----------------------------------------------------------
	 * <p>
	 * <p>
	 * Order of Growth
	 * -------------------------
	 * <p>
	 * Caclulate and write down the order of growth of your algorithm. You can use the asymptotic notation.
	 * You should adequately explain your answer. Answers without adequate explanation will not be counted.
	 * <p>
	 * Order of growth: N^3
	 * <p>
	 * Explanation: Three linear for-loops.
	 */
	static int countCollinear(int[] a1, int[] a2, int[] a3) {
		int count = 0;

		for (int i : a1) {
			for (int j : a2) {
				for (int k : a3) {
					if (i*(2-3) + j*(3-1) + k*(1-2) == 0) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * Counts for the number of non-horizontal lines that go through 3 points in arrays a1, a2, a3.
	 * This method is static, thus it can be called as Collinear.countCollinearFast(a1,a2,a3)
	 *
	 * @param a1: An UNSORTED array of integers. Each integer a1[i] represents the point (a1[i], 1) on the plain.
	 * @param a2: An UNSORTED array of integers. Each integer a2[i] represents the point (a2[i], 2) on the plain.
	 * @param a3: An UNSORTED array of integers. Each integer a3[i] represents the point (a3[i], 3) on the plain.
	 * @return the number of points which are collinear and do not lie on a horizontal line.
	 * <p>
	 * In this implementation you should make non-trivial use of InsertionSort and Binary Search.
	 * The performance of this method should be much better than that of the above method.
	 * <p>
	 * <p>
	 * Order of Growth
	 * -------------------------
	 * <p>
	 * Caclulate and write down the order of growth of your algorithm. You can use the asymptotic notation.
	 * You should adequately explain your answer. Answers without adequate explanation will not be counted.
	 * <p>
	 * Order of Growth: N^2 log N
	 * <p>
	 * Explanation: Two linear for-loops with a binary search.
	 */
	static int countCollinearFast(int[] a1, int[] a2, int[] a3) {
		int count = 0;
		sort(a3);

		// x1*(y2-y3) + x2*(y3-y1) + x3*(y1-y2) = 0: collinear
		// x1*(y2-y3) + x2*(y3-y1) = -(x3*(y1-y2))
		// -(x1*(y2-y3) + x2*(y3-y1)) = x3*(y1-y2)
		// -(x1*(y2-y3) + x2*(y3-y1)) / (y1-y2) = x3
		for (int i = 0; i < a1.length; i++) {
			for (int j = 0; j < a2.length; j++) {
				int toFind = -(a1[i]*(2-3) + a2[j]*(3-1)) / (1-2);
				if (binarySearch(a3, toFind)) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Sorts an array of integers according to InsertionSort.
	 * This method is static, thus it can be called as Collinear.sort(a)
	 *
	 * @param a: An UNSORTED array of integers.
	 * @return after the method returns, the array must be in ascending sorted order.
	 * <p>
	 * ----------------------------------------------------------
	 * <p>
	 * Performance: N^2
	 * <p>
	 * Explanation: Two linear for-loops.
	 */
	static void sort(int[] a) {
		for (int j = 1; j < a.length; j++) {
			int i = j - 1;
			while (i >= 0 && a[i] > a[i + 1]) {
				int temp = a[i];
				a[i] = a[i + 1];
				a[i + 1] = temp;
				i--;
			}
		}
	}

	/**
	 * Searches for an integer inside an array of integers.
	 * This method is static, thus it can be called as Collinear.binarySearch(a,x)
	 *
	 * @param a: A array of integers SORTED in ascending order.
	 * @param x: An integer.
	 * @return true if 'x' is contained in 'a'; false otherwise.
	 * <p>
	 * ----------------------------------------------------------
	 * <p>
	 * Performance: log N
	 * <p>
	 * Explanation: Single loop where N is cut in half on each loop
	 */
	static boolean binarySearch(int[] a, int x) {
		int left = 0;
		int right = a.length - 1;
		while (left <= right) {
			int mid = (left + right) / 2;
			if (a[mid] < x) {
				left = mid + 1;
			} else if (a[mid] > x) {
				right = mid - 1;
			} else {
				return true;
			}
		}

		return false;
	}
}
