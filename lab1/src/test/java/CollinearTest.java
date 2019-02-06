import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Callable;

// If XChart library is available
/*import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;*/

/**
 * Test class for Collinear.java
 *
 * @author Jack O'Sullivan
 * @version 18/09/18 9:15:51
 */
@RunWith(JUnit4.class)
public class CollinearTest {
	private static String methodCall(boolean fast, int[] a1, int[] a2, int[] a3) {
		StringBuilder sb = new StringBuilder("countCollinear");
		if (fast) {
			sb.append("Fast");
		}
		sb
				.append('(')
				.append(Arrays.toString(a1)).append(", ")
				.append(Arrays.toString(a2)).append(", ")
				.append(Arrays.toString(a3)).append(')');

		return sb.toString();
	}
	private static void testBoth(int expectedResult, int[] a1, int[] a2, int[] a3) {
		assertEquals(methodCall(false, a1, a2, a3), expectedResult, Collinear.countCollinear(a1, a2, a3));
		assertEquals(methodCall(true, a1, a2, a3), expectedResult, Collinear.countCollinearFast(a1, a2, a3));
	}

	@Test
	public void testConstructor() {
		new Collinear();
	}

	/**
	 * Check that the two methods work for empty arrays
	 */
	@Test
	public void testEmpty() {
		testBoth(0, new int[0], new int[0], new int[0]);
	}

	/**
	 * Check for no false positives in a single-element array
	 */
	@Test
	public void testSingleFalse() {
		testBoth(0, new int[] {15}, new int[] {5}, new int[] {10});
	}

	/**
	 * Check for a single collinear set and no false positives
	 */
	@Test
	public void testSingleTrue() {
		testBoth(1, new int[] {15, 5}, new int[] {5}, new int[] {10, 15, 5});
	}

	/**
	 * Check for no false positives with multiple collinear sets
	 */
	@Test
	public void testMultiTrue() {
		testBoth(3, new int[] {10, -4, 2, 123}, new int[] {887, 4, 20, -8}, new int[] {6, 30, 103, -12});
	}

	private static double average(long[] items) {
		long average = 0;
		for (long i : items) {
			average += i;
		}

		return average / (double)items.length;
	}
	private static long measure(Callable fn) throws Exception {
		long start = System.currentTimeMillis();
		fn.call();
		return System.currentTimeMillis() - start;
	}
	public static void perf(int[] sizes, int runs) throws IOException {
		Random rand = new Random();

		double[] bfTimes = new double[sizes.length];
		double[] bsTimes = new double[sizes.length];

		long start = System.currentTimeMillis();
		for (int j = 0; j < sizes.length; j++) {
			int n = sizes[j];
			final int[] a1 = new int[n];
			final int[] a2 = new int[n];
			final int[] a3 = new int[n];
			long[] bft = new long[runs];
			long[] bst = new long[runs];

			for (int run = 0; run < runs; run++) {
				for (int i = 0; i < n; i++) {
					double fn = rand.nextDouble() * 10;
					int constant = rand.nextInt(10);

					a1[i] = (int)((1 - constant) / fn);
					a2[i] = (int)((2 - constant) / fn);
					a3[i] = (int)((3 - constant) / fn);
				}

				try {
					// Java 8 on WebCAT plz???
					//bft[run] = measure(() -> Collinear.countCollinear(a1, a2, a3));
					//bst[run] = measure(() -> Collinear.countCollinearFast(a1, a2, a3));

					measure(new Callable() {
						@Override
						public Object call() {
							return Collinear.countCollinear(a1, a2, a3);
						}
					});
					measure(new Callable() {
						@Override
						public Object call() {
							return Collinear.countCollinearFast(a1, a2, a3);
						}
					});
				} catch (Exception ex) {
					ex.printStackTrace();
					System.exit(-1);
				}
			}

			// Java 8 on WebCAT plz???
			//bfTimes[j] = LongStream.of(bft).average().getAsDouble() / 1000.0;
			//bsTimes[j] = LongStream.of(bst).average().getAsDouble() / 1000.0;
			bfTimes[j] = average(bft) / 1000.0;
			bsTimes[j] = average(bst) / 1000.0;
		}

		// Graph using XChart library
		/*double[] dSizes = IntStream.of(sizes).asDoubleStream().toArray();
		XYChart chart = new XYChartBuilder().width(1280).height(720).title("Collinear").xAxisTitle("n").yAxisTitle("t").build();
		chart.getStyler().setXAxisLogarithmic(true).setYAxisLogarithmic(true);

		chart.addSeries("Brute force", dSizes, bfTimes);
		chart.addSeries("Binary search", dSizes, bsTimes);
		BitmapEncoder.saveBitmap(chart, "results", BitmapEncoder.BitmapFormat.PNG);*/

		for (int j = 0; j < sizes.length; j++) {
			System.out.printf("n = %d:\n", sizes[j]);
			System.out.printf("Brute force   : %fs\n", bfTimes[j]);
			System.out.printf("Binary search : %fs\n", bsTimes[j]);
		}
		System.out.printf("Total run time : %fs\n", (System.currentTimeMillis() - start) / 1000.0);
	}

	/**
	 * Main Method.
	 * Use this main method to create the experiments needed to answer the experimental performance questions of this assignment.
	 * <br />
	 * You should read the lecture notes and/or book to understand how to correctly implement the main methods.
	 * You can use any of the provided classes to read from files, and time your code.
	 */
	public static void main(String[] args) throws Exception {
		int[] sizes = new int[] { 200, 500, 700, 1000, 1250, 1600, 2000 };
		perf(sizes, 3);
	}
}
