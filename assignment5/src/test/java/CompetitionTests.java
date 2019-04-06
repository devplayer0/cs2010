import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * 1. Justify the choice of the data structures used in CompetitionDijkstra and CompetitionFloydWarshall
 *
 *    Graph: Shared between CompetitionDijkstra and CompetitionFloydWarshall. Uses an adjacency list implementation of a
 *    graph. This makes sense since both algorithms iterate over the adjacent vertices for each vertex in the graph.
 *
 *    CompetitionDijkstra: The most important data structure in the Dijkstra shortest path implementation is the
 *    unvisited set. The simplest implementation uses an unordered list with linear search for the closest next vertex.
 *    However, a binary heap-based priority queue implementation (with decrease-key) greatly improves performance with
 *    sparse graphs. This definitely applies in this case since the graph represents a city. MinHeap implements a heap
 *    with the required `extractMin()` function. IndexingMinHeap makes use of a HashMap to cache the indices into the
 *    heap array. This is necessary for PriorityQueue, which implements `changePriority()`. This method looks up the
 *    index from the HashMap and then changes its priority and calls `bubbleUp()` / `sinkDown()` accordingly.
 *
 *    Since this implementation uses an abstract `Competition` class (which implements the minimum time search based on
 *    a matrix of shortest paths returned by a subclass's `findDistances()` method), CompetitionDijkstra runs Dijkstra's
 *    algorithm for every vertex in the graph. It is also worth noting that the `prevNode` map is not used, since the
 *    only the distances are relevant to the assignment.
 *
 *    CompetitionFloydWarshall: There is relatively little to discuss here that is not the same as the Dijkstra
 *    implementation. Floyd-Warshall manipulates the distance matrix directly and produces the distances for all pairs.
 *
 * 2. Explain theoretical differences in the performance of Dijkstra and Floyd-Warshall algorithms
 *    in the given problem. Also explain how would their relative performance be affected by the
 *    density of the graph. Which would you choose in which set of circumstances and why?
 *
 *    Thanks to the binary heap, the Dijkstra implementation runs in O(VE log V), whereas the Floyd-Warshall
 *    implementation runs in O(V^3). With a larger "city", this shows a serious performance improvement. It is
 *    worth noting that Floyd-Warshall only requires the graph and the result matrix, whereas also Dijkstra needs a
 *    minheap with `decreaseKey()` (which, as explained above, stores a HashMap of all the vertices as well as the array
 *    backing the heap itself).
 *
 *    Since a "city" is a relatively sparse graph, it makes more sense to use Dijkstra than Floyd-Warshall in this case
 *    - the performance of Dijkstra is dependant on the number of edges, whereas Floyd-Warshall is only dependant on the
 *    number of vertices. If the graph was much more dense (and the graph was very large), it would make more sense to
 *    use Floyd-Warshall.
 */
public class CompetitionTests {
    @Test
    public void testBasicGraph() {
        Graph<Integer, Integer> graph = new Graph<>();
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);

        assertFalse(graph.addVertex(0));
        assertTrue(graph.removeVertex(2));
        assertFalse(graph.removeVertex(2));
        assertEquals(2, graph.vertexCount());
        assertEquals(new HashSet<>(Arrays.asList(0, 1)), graph.vertices());
        assertEquals(0, graph.edgeCount());
        assertEquals(0, graph.getAdjacent(0).size());
        assertFalse(graph.isAdjacent(0, 1));
    }
    @Test
    public void testGraphEdges() {
        Graph<Integer, Integer> graph = new Graph<>();
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);

        assertTrue(graph.addEdge(0, 1, 5));
        assertFalse(graph.addEdge(0, 1, 5));
        graph.addEdge(1, 0, 3);
        graph.addEdge(2, 0, 7);
        graph.addEdge(3, 4, 7);
        graph.addEdge(5, 9, 2);
        assertEquals(7, graph.vertexCount());
        assertEquals(5, graph.edgeCount());

        assertTrue(graph.removeEdge(5, 9));
        assertFalse(graph.removeEdge(5, 9));
        graph.removeVertex(9);
        assertFalse(graph.removeEdge(5, 9));
        graph.removeVertex(5);
        assertFalse(graph.removeEdge(5, 9));
        assertEquals(4, graph.edgeCount());

        assertTrue(graph.removeVertex(0));
        assertEquals(1, graph.edgeCount());
    }
    @Test
    public void testGraphAdjacency() {
        Graph<Integer, Integer> graph = new Graph<>();
        graph.addEdge(0, 1, 3);
        graph.addEdge(1, 0, 4);
        graph.addEdge(2, 0, 7);
        graph.addEdge(3, 4, 7);
        graph.addEdge(5, 9, 2);

        assertTrue(graph.isAdjacent(0, 1));
        assertTrue(graph.isAdjacent(1, 0));
        assertTrue(graph.isAdjacent(5, 9));
        assertFalse(graph.isAdjacent(9, 5));
        assertFalse(graph.isAdjacent(0, 0));
        assertFalse(graph.isAdjacent(1337, 1338));

        assertEquals(3, (int)graph.getEdgeWeight(0, 1));
        assertEquals(4, (int)graph.getEdgeWeight(1, 0));
        assertNull(graph.getEdgeWeight(99, 98));
        assertNull(graph.getEdgeWeight(9, 99));
        assertNull(graph.getEdgeWeight(9, 5));
    }
    @Test
    public void testGraphParsing() throws IOException {
        Graph<Integer, Double> graph = Graph.parseFromStream(getClass().getResourceAsStream("tinyEWD.txt"));
        assertEquals(8, graph.vertexCount());
        assertEquals(15, graph.edgeCount());

        try {
            Graph.parseFromStream(getClass().getResourceAsStream("badGraph.txt"));
            fail();
        } catch (IOException e) {
            assertEquals("Input line 3 contains an invalid edge", e.getMessage());
        }

        Graph<Integer, Double> graph3 = Graph.parseFromStream(getClass().getResourceAsStream("1000EWD.txt"));
        assertEquals(1000, graph3.vertexCount());
        assertEquals(16866, graph3.edgeCount());
    }

    @Test
    public void testHeapPassthrough() {
        // Purely for coverage, these just pass through to the internal ArrayList
        MinHeap<Double> heap = new MinHeap<>();
        new MinHeap<>(1);
        heap.size();
        heap.isEmpty();
        heap.contains(null);
        heap.iterator();
        heap.toArray();
        heap.toArray(new Double[0]);
        heap.containsAll(new ArrayList<>());
        heap.offer(123.);
        heap.element();
        heap.peek();
        heap.poll();
        heap.offer(123.);
        heap.remove();
        heap.clear();

        try {
            heap.remove(null);
            fail();
        } catch (UnsupportedOperationException ex) {}
        try {
            heap.removeAll(null);
            fail();
        } catch (UnsupportedOperationException ex) {}
        try {
            heap.retainAll(null);
            fail();
        } catch (UnsupportedOperationException ex) {}

        try {
            heap.element();
            fail();
        } catch (NoSuchElementException ex) {}
        try {
            heap.remove();
            fail();
        } catch (NoSuchElementException ex) {}
    }
    @Test
    public void testHeap() {
        MinHeap<Double> heap = new MinHeap<>();
        assertNull(heap.getMin());
        assertNull(heap.extractMin());

        heap.addAll(Arrays.asList(5., 3., 9., 0., 1., 1., -2., 11.));

        assertEquals(-2., heap.getMin(), 0);
        assertEquals(-2., heap.extractMin(), 0);
        assertEquals(0., heap.extractMin(), 0);
        assertEquals(1., heap.extractMin(), 0);
        assertEquals(1., heap.extractMin(), 0);
        assertEquals(3., heap.extractMin(), 0);
        assertEquals(5., heap.extractMin(), 0);
    }
    @Test
    public void testIndexingHeap() {
        IndexingMinHeap<Integer> heap = new IndexingMinHeap<>();
        heap.addAll(Arrays.asList(5, 3, 9, 0, 1, 1, -2, 11));

        for (int i = 0; i < heap.size(); i++) {
            assertEquals(i, heap.indexOf(heap.heap.get(i)));
        }
        heap.extractMin();
        heap.extractMin();
        heap.extractMin();
        assertTrue(heap.contains(9));
        assertTrue(heap.containsAll(Arrays.asList(9, 5, 3)));
        for (int i = 0; i < heap.size(); i++) {
            assertEquals(i, heap.indexOf(heap.heap.get(i)));
        }
        heap.clear();
        assertTrue(heap.isEmpty());
        assertTrue(heap.indices.isEmpty());

        heap = new IndexingMinHeap<>(1);
        assertNull(heap.extractMin());
        assertEquals(-1, heap.indexOf(123));
    }
    @Test
    public void testPriorityQueue() {
        PriorityQueue<String, Double> queue = new PriorityQueue<>();
        assertNull(queue.getMin());
        assertNull(queue.extractMin());

        // equals() for coverage...
        PriorityQueue.QueueItem i = queue.new QueueItem("test", 2.);
        assertEquals(i, i);
        assertFalse(i.equals(null));
        assertFalse(i.equals("test"));
        assertEquals("test: 2.0", i.toString());
        assertNotEquals(queue.new QueueItem("lol", 1.), i);
        assertEquals(queue.new QueueItem("test", 2.), i);

        queue.insert("test", 1.);
        queue.insert("lol", 3.);
        queue.insert("lel", -1.);
        queue.insert("1337", 0.);
        queue.insert("zoop", 8.);
        assertEquals("lel", queue.getMin());
        assertFalse(queue.insert("1337", 0.));

        assertEquals("lel", queue.extractMin());
        assertEquals("1337", queue.extractMin());
        assertNull(queue.getPriority("123123123"));
        assertEquals(3., queue.getPriority("lol"), 0);

        assertFalse(queue.changePriority("lol", 3.));
        assertEquals("test", queue.getMin());
        assertTrue(queue.changePriority("lol", -5.));
        assertEquals("lol", queue.getMin());
        assertTrue(queue.changePriority("test", 5.));

        assertTrue(queue.changePriority("asdf", -50.));
        assertEquals("asdf", queue.getMin());
        queue.clear();
        assertTrue(queue.isEmpty());

        Map<String, Double> map = new HashMap<>();
        map.put("qwe", 5.);
        map.put("rty", 7.);
        map.put("zxc", 1.);
        queue = new PriorityQueue<>(3);
        queue.insertAll(map);
        assertEquals(3, queue.size());
        assertEquals("zxc", queue.getMin());
    }

    private static Competition instantiateComp(Class<? extends Competition> compClass, String filename, int sA, int sB, int sC) {
        try {
            return compClass
                    .getDeclaredConstructor(String.class, int.class, int.class, int.class)
                    .newInstance(filename, sA, sB, sC);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalArgumentException(String.format("Failed to instantiate %s: %s", compClass, ex));
        }
    }
    @Test
    public void testCompetitionConstructors() {
        instantiateComp(CompetitionDijkstra.class, "res:1000EWD.txt", 50, 60, 70);
        instantiateComp(CompetitionDijkstra.class, "src/test/resources/1000EWD.txt", 50, 60, 70);

        instantiateComp(CompetitionFloydWarshall.class, "res:1000EWD.txt", 1, 2, 3);
        instantiateComp(CompetitionFloydWarshall.class, "src/test/resources/1000EWD.txt", 50, 60, 70);

        // Check out of range speeds
        Competition comp = instantiateComp(CompetitionDijkstra.class, "res:1000EWD.txt", 0, 0, 0);
        assertEquals(-1, comp.timeRequiredforCompetition());
        comp.sA = 50;
        assertEquals(-1, comp.timeRequiredforCompetition());
        comp.sB = 50;
        assertEquals(-1, comp.timeRequiredforCompetition());
        comp.sC = 50;
        assertNotEquals(-1, comp.timeRequiredforCompetition());
        comp.sC = 101;
        assertEquals(-1, comp.timeRequiredforCompetition());
        comp.sB = 101;
        assertEquals(-1, comp.timeRequiredforCompetition());
        comp.sA = 101;
        assertEquals(-1, comp.timeRequiredforCompetition());
    }

    private void testDistances(Competition comp) {
        double[][] dist = comp.findDistances();
        assertEquals(1.53, dist[5][2], 0.0001);
        assertEquals(1.13, dist[5][6], 0.0001);
        assertEquals(1.71, dist[5][0], 0.0001);

        assertEquals(0.97, dist[2][4], 0.0001);
        assertEquals(0.94, dist[2][1], 0.0001);
        assertEquals(1.83, dist[2][0], 0.0001);
    }
    @Test
    public void testDistances() {
        testDistances(instantiateComp(CompetitionDijkstra.class, "res:tinyEWD.txt", 50, 60, 70));
        testDistances(instantiateComp(CompetitionFloydWarshall.class, "res:tinyEWD.txt", 50, 60, 70));
    }

    private void testCompetition(Class<? extends Competition> compClass) {
        Competition comp1 = instantiateComp(compClass, "res:tinyEWD.txt", 50, 60, 70);
        assertEquals(1, comp1.timeRequiredforCompetition());

        Competition comp2 = instantiateComp(compClass, "res:badCity.txt", 50, 60, 70);
        assertEquals(-1, comp2.timeRequiredforCompetition());

        Competition comp3 = instantiateComp(compClass, "res:1000EWD.txt", 50, 60, 70);
        assertEquals(1, comp3.timeRequiredforCompetition());
    }
    @Test
    public void testDijkstraCompetition() {
        testCompetition(CompetitionDijkstra.class);
    }
    @Test
    public void testFWCompetition() {
        testCompetition(CompetitionFloydWarshall.class);
    }
}
