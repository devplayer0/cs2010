import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

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

    @Test
    public void testDijkstraConstructor() throws IOException {
        new CompetitionDijkstra("res:1000EWD.txt", 1, 2, 3);
        new CompetitionDijkstra("src/test/resources/1000EWD.txt", 1, 2, 3);
    }
    @Test
    public void testDijkstraDistances() throws IOException {
        Competition comp = new CompetitionDijkstra("res:tinyEWD.txt", 1, 2, 3);
        Map<Integer, Double> dist1 = comp.findDistances(5);
        assertEquals(1.53, dist1.get(2), 0.0001);
        assertEquals(1.13, dist1.get(6), 0.0001);
        assertEquals(1.71, dist1.get(0), 0.0001);

        Map<Integer, Double> dist2 = comp.findDistances(2);
        assertEquals(0.97, dist2.get(4), 0.0001);
        assertEquals(0.94, dist2.get(1), 0.0001);
        assertEquals(1.83, dist2.get(0), 0.0001);
    }
    @Test
    public void testDijkstraCompetition() throws IOException {
        Competition comp1 = new CompetitionDijkstra("res:tinyEWD.txt", 1, 2, 3);
        assertEquals(2, comp1.timeRequiredforCompetition());

        Competition comp2 = new CompetitionDijkstra("res:badCity.txt", 1, 2, 3);
        assertEquals(-1, comp2.timeRequiredforCompetition());
    }

    @Test
    public void testFWConstructor() {
        // TODO: implement
    }

    // TODO: add more tests
}
