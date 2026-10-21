package shortestpath;

public class PathResult {
    public final int source;
    public final double[] distances;
    public final int[] parents;

    public PathResult(int source, double[] distances, int[] parents) {
        this.source = source;
        this.distances = distances;
        this.parents = parents;
    }
}