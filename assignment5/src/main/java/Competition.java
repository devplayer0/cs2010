import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Map;

/*
 * A Contest to Meet (ACM) is a reality TV contest that sets three contestants at three random
 * city intersections. In order to win, the three contestants need all to meet at any intersection
 * of the city as fast as possible.
 * It should be clear that the contestants may arrive at the intersections at different times, in
 * which case, the first to arrive can wait until the others arrive.
 * From an estimated walking speed for each one of the three contestants, ACM wants to determine the
 * minimum time that a live TV broadcast should last to cover their journey regardless of the contestants'
 * initial positions and the intersection they finally meet. You are hired to help ACM answer this question.
 * You may assume the following:
 *    1. Each contestant walks at a given estimated speed.
 *    2. The city is a collection of intersections in which some pairs are connected by one-way
 * streets that the contestants can use to traverse the city.
 *
 */
public abstract class Competition {
    protected Graph<Integer, Double> city;
    protected int sA, sB, sC;

    /**
     * @param filename: A filename containing the details of the city road network
     * @param sA, sB, sC: speeds for 3 contestants
     */
    public Competition(String filename, int sA, int sB, int sC) throws IOException {
        InputStream in = filename.startsWith("res:") ?
                getClass().getResourceAsStream(filename.substring("res:".length())) :
                new FileInputStream(filename);

        this.city = Graph.parseFromStream(in);
        this.sA = sA;
        this.sB = sB;
        this.sC = sC;
    }

    abstract public Map<Integer, Double> findDistances(int start);

    private double minTimeRecurse(Map<Integer, Double> distances, Deque<Integer> speeds, double minTime) {
        int speed = speeds.remove();
        for (double distance : distances.values()) {
            if (distance == Double.POSITIVE_INFINITY) {
                return -1;
            }

            double time = distance / (double)speed;
            if (time > minTime) {
                minTime = time;
            }
        }

        return speeds.isEmpty() ? minTime : minTimeRecurse(distances, speeds, minTime);
    }
    /**
     * @return int: minimum minutes that will pass before the three contestants can meet
     */
    public int timeRequiredforCompetition() {
        double minTime = -1;
        for (int intersection : city.vertices()) {
            Map<Integer, Double> distances = findDistances(intersection);
            Deque<Integer> speeds = new ArrayDeque<>(Arrays.asList(sA, sB, sC));
            double intersectionTime = minTimeRecurse(distances, speeds, -1);

            if (intersectionTime == -1) {
                return -1;
            }
            if (intersectionTime > minTime) {
                minTime = intersectionTime;
            }
        }

        return (int)Math.ceil(minTime);
    }
}
