import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
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
    public void testDijkstraConstructor() {
        // TODO: implement
    }

    @Test
    public void testFWConstructor() {
        // TODO: implement
    }

    // TODO: add more tests
}
