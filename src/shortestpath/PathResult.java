package shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathResult {
    public final int source;
    public final double[] distances;
    public final int[] parents;
    public final long edgesExamined;
    public final long relaxations;
    public final long pqPushes;
    public final long pqPops;
    public final long comparisons;
    public final long timeNanos;

    public PathResult(int source, double[] distances, int[] parents,
                      long edgesExamined, long relaxations,
                      long pqPushes, long pqPops, long comparisons, long timeNanos) {
        this.source = source;
        this.distances = distances;
        this.parents = parents;
        this.edgesExamined = edgesExamined;
        this.relaxations = relaxations;
        this.pqPushes = pqPushes;
        this.pqPops = pqPops;
        this.comparisons = comparisons;
        this.timeNanos = timeNanos;
    }

    public boolean isReachable(int v) {
        return distances[v] != Double.POSITIVE_INFINITY;
    }

    public List<Integer> getPath(int target) {
        List<Integer> path = new ArrayList<>();
        if (!isReachable(target)) {
            return path;
        }
        for (int v = target; v != -1; v = parents[v]) {
            path.add(v);
        }
        Collections.reverse(path);
        return path;
    }
}