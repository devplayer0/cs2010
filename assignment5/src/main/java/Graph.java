import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Graph<V, W extends Comparable<W>> {
    private Map<V, Map<V, W>> adjacency;
    public Graph() {
        adjacency = new HashMap<>();
    }

    public boolean addVertex(V vertex) {
        if (adjacency.containsKey(vertex)) {
            return false;
        }

        adjacency.put(vertex, new HashMap<V, W>());
        return true;
    }
    public boolean containsVertex(V vertex) {
        return adjacency.containsKey(vertex);
    }
    public boolean removeVertex(V vertex) {
        if (!containsVertex(vertex)) {
            return false;
        }

        adjacency.remove(vertex);
        for (Map<V, W> adj : adjacency.values()) {
            adj.remove(vertex);
        }
        return true;
    }

    public boolean addEdge(V a, V b, W weight) {
        if (!containsVertex(a)) {
            addVertex(a);
        }
        if (!containsVertex(b)) {
            addVertex(b);
        }

        return adjacency.get(a).put(b, weight) == null;
    }
    public W getEdgeWeight(V a, V b) {
        if (!containsVertex(a) || !containsVertex(b) || !isAdjacent(a, b)) {
            return null;
        }

        return adjacency.get(a).get(b);
    }
    public boolean removeEdge(V a, V b) {
        if (!containsVertex(a) || !containsVertex(b)) {
            return false;
        }

        return adjacency.get(a).remove(b) != null;
    }

    public Iterable<V> getAdjacent(V vertex) {
        return adjacency.get(vertex).keySet();
    }
    public boolean isAdjacent(V a, V b) {
        return containsVertex(a) && adjacency.get(a).containsKey(b);
    }

    private static final Pattern EDGE_PATTERN = Pattern.compile("^\\s*(?<a>\\d+)\\s*(?<b>\\d+)\\s*(?<weight>\\d+\\.\\d+)$");
    public static Graph<Integer, Double> parseFromStream(InputStream in) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        int nVertices = Integer.parseInt(r.readLine());
        int nEdges = Integer.parseInt(r.readLine());

        Graph<Integer, Double> graph = new Graph<>();
        for (int i = 0; i < nEdges; i++) {
            Matcher m = EDGE_PATTERN.matcher(r.readLine());
            if (!m.matches()) {
                throw new IOException(String.format("Input line %d contains an invalid edge", i));
            }

            int a = Integer.parseInt(m.group("a"));
            int b = Integer.parseInt(m.group("b"));
            double weight = Double.parseDouble(m.group("weight"));
            graph.addEdge(a, b, weight);
        }

        return graph;
    }
}
