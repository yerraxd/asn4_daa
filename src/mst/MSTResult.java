package mst;

import graph.Edge;
import java.util.List;

public class MSTResult {
    public final List<Edge> edges;
    public final double totalWeight;
    public final boolean connected;
    public final long edgesExamined;
    public final long comparisons;

    public MSTResult(List<Edge> edges, double totalWeight, boolean connected,
                     long edgesExamined, long comparisons) {
        this.edges = edges;
        this.totalWeight = totalWeight;
        this.connected = connected;
        this.edgesExamined = edgesExamined;
        this.comparisons = comparisons;
    }
}