package mst;

import graph.Edge;
import java.util.List;

public class MSTResult {
    public final List<Edge> edges;
    public final double totalWeight;
    public final boolean connected;
    public final long edgesExamined;
    public final long comparisons;
    public final long findOps;
    public final long unionOps;
    public final long pqPushes;
    public final long pqPops;
    public final long timeNanos;

    public MSTResult(List<Edge> edges, double totalWeight, boolean connected,
                     long edgesExamined, long comparisons,
                     long findOps, long unionOps,
                     long pqPushes, long pqPops, long timeNanos) {
        this.edges = edges;
        this.totalWeight = totalWeight;
        this.connected = connected;
        this.edgesExamined = edgesExamined;
        this.comparisons = comparisons;
        this.findOps = findOps;
        this.unionOps = unionOps;
        this.pqPushes = pqPushes;
        this.pqPops = pqPops;
        this.timeNanos = timeNanos;
    }
}