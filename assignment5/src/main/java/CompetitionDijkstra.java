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
    public Map<Integer, Double> findDistances(int source) {
        // We don't actually need to calculate the path that can be taken
        //Map<Integer, Integer> prevMap = new HashMap<>();
        Map<Integer, Double> distances = new HashMap<>();
        PriorityQueue<Integer, Double> unvisited = new PriorityQueue<>();

        distances.put(source, 0.);
        for (int vertex : city.vertices()) {
            if (vertex != source) {
                distances.put(vertex, Double.POSITIVE_INFINITY);
            }

            unvisited.insert(vertex, distances.get(vertex));
        }

        while (!unvisited.isEmpty()) {
            int best = unvisited.extractMin();
            for (int neighbour : city.getAdjacent(best)) {
                double distance = distances.get(best) + city.getEdgeWeight(best, neighbour);
                if (distance < distances.get(neighbour)) {
                    distances.put(neighbour, distance);
                    //prevMap.put(neighbour, best);
                    unvisited.changePriority(neighbour, distance);
                }
            }
        }

        return distances;
    }
}