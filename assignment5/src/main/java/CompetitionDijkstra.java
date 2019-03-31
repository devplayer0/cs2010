import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * This class implements the competition using Dijkstra's algorithm
 */
public class CompetitionDijkstra extends Competition {
    public CompetitionDijkstra(String filename, int sA, int sB, int sC) throws IOException {
        super(filename, sA, sB, sC);
    }

    @Override
    public double[][] findDistances() {
        double[][] allDistances = new double[city.vertexCount()][city.vertexCount()];
        for (int source : city.vertices()) {
            // We don't actually need to calculate the path that can be taken
            //Map<Integer, Integer> prevMap = new HashMap<>();
            double[] distances = allDistances[source];
            PriorityQueue<Integer, Double> unvisited = new PriorityQueue<>();

            distances[source] = 0.;
            for (int vertex : city.vertices()) {
                if (vertex != source) {
                    distances[vertex] = Double.POSITIVE_INFINITY;
                }

                unvisited.insert(vertex, distances[vertex]);
            }

            while (!unvisited.isEmpty()) {
                int best = unvisited.extractMin();
                for (int neighbour : city.getAdjacent(best)) {
                    double distance = distances[best] + city.getEdgeWeight(best, neighbour);
                    if (distance < distances[neighbour]) {
                        distances[neighbour] = distance;
                        //prevMap.put(neighbour, best);
                        unvisited.changePriority(neighbour, distance);
                    }
                }
            }
        }

        return allDistances;
    }
}