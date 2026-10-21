package shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathResult {
    public final int source;
    public final double[] distances;
    public final int[] parents;

    public PathResult(int source, double[] distances, int[] parents) {
        this.source = source;
        this.distances = distances;
        this.parents = parents;
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