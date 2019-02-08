import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    private static final SortedMap<String, String> TEST_FILES = new TreeMap<>();
    static {
        TEST_FILES.put("numbers10.txt", "10 random");
        TEST_FILES.put("numbers100.txt", "100 random");
        TEST_FILES.put("numbers1000.txt", "1000 random");
        TEST_FILES.put("numbers1000Duplicates.txt", "1000 few unique");
        TEST_FILES.put("numbersNearlyOrdered1000.txt", "1000 nearly ordered");
        TEST_FILES.put("numbersReverse1000.txt", "1000 reverse order");
        TEST_FILES.put("numbersSorted1000.txt", "1000 sorted");
    }

    private static class Results extends ConcurrentHashMap<String, Long> {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Map.Entry<String, Long> e : entrySet()) {
                sb.append(e.getKey()).append(": ").append(String.format("%.3f", e.getValue() / 1000.0)).append("us");
                if (i != size() - 1) {
                    sb.append(", ");
                }
                i++;
            }

            return sb.toString();
        }
    }
    private static class Experiment extends Thread {
        private final String filename;
        private int runs;
        private double[] numbers;
        private ThreadMXBean mxBean;
        private Results results;

        private HashMap<String, Method> methods;
        public Experiment(String filename, int runs) {
            super("Experiment-"+filename);
            this.filename = filename;
            this.runs = runs;
            mxBean = ManagementFactory.getThreadMXBean();
            results = new Results();

            loadMethods();
        }

        private void loadMethods() {
            methods = new HashMap<>();

            for (Method sort : SortComparison.class.getDeclaredMethods()) {
                if (!Modifier.isPublic(sort.getModifiers())) {
                    continue;
                }

                String method = sort.getName();
                methods.put(method, sort);
                results.put(method, 0L);
            }
        }
        private double[] readFile() throws IOException {
            List<Double> numList = new ArrayList<>();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename)))) {
                for (String line; (line = in.readLine()) != null;) {
                    numList.add(Double.parseDouble(line));
                }
            }

            double[] numbers = new double[numList.size()];
            for (int i = 0; i < numbers.length; i++) {
                numbers[i] = numList.get(i);
            }

            return numbers;
        }
        @Override
        public void run() {
            try {
                numbers = readFile();
                List<Runner> threads = new ArrayList<>(methods.size() * runs);
                Results[] results = new Results[runs];
                for (int run = 0; run < runs; run++) {
                    results[run] = new Results();

                    for (String method : methods.keySet()) {
                        Runner runner = new Runner(method, results[run], run);
                        threads.add(runner);
                        runner.start();
                    }
                }

                for (Runner r : threads) {
                    r.join();
                }
                for (Results r : results) {
                    for (Map.Entry<String, Long> result : r.entrySet()) {
                        this.results.put(result.getKey(), this.results.get(result.getKey()) + result.getValue());
                    }
                }
                for (Map.Entry<String, Long> result : this.results.entrySet()) {
                    this.results.put(result.getKey(), result.getValue() / runs);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        private class Runner extends Thread {
            private String method;
            private Method sort;
            private Results results;
            public Runner(String method, Results results, int run) {
                super("Experiment-"+filename+"-"+method+"-"+run);
                this.method = method;
                this.results = results;
                sort = Experiment.this.methods.get(method);
            }

            @Override
            public void run() {
                try {
                    long start = mxBean.getCurrentThreadCpuTime();
                    sort.invoke(null, numbers);
                    long duration = mxBean.getCurrentThreadCpuTime() - start;

                    results.put(method, duration);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Experiment[] experiments = new Experiment[TEST_FILES.size()];
        int i = 0;
        for (String filename : TEST_FILES.keySet()) {
            Experiment experiment = new Experiment(filename, 1000);
            experiment.start();
            experiments[i++] = experiment;
        }

        for (Experiment e : experiments) {
            e.join();
            System.out.printf("%s: %s\n", TEST_FILES.get(e.filename), e.results);
        }
    }
}
