import java.util.Arrays;

/*
 * This class implements the competition using the Floyd-Warshall algorithm
 */
public class CompetitionFloydWarshall extends Competition {
    public CompetitionFloydWarshall(String filename, int sA, int sB, int sC) {
        super(filename, sA, sB, sC);
    }

    @Override
    public double[][] findDistances() {
        double[][] dist = new double[city.vertexCount()][city.vertexCount()];
        for (double[] row : dist) {
            Arrays.fill(row, Double.POSITIVE_INFINITY);
        }

        for (int vertex : city.vertices()) {
            for (int neighbor : city.getAdjacent(vertex)) {
                dist[vertex][neighbor] = city.getEdgeWeight(vertex, neighbor);
            }

            dist[vertex][vertex] = 0;
        }

        for (int k : city.vertices()) {
            for (int i : city.vertices()) {
                for (int j : city.vertices()) {
                    if (dist[i][j] > dist[i][k] + dist[k][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        return dist;
    }
}
