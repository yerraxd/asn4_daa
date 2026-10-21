package mst;

import graph.Edge;
import java.util.List;

public class MSTResult {
    public final List<Edge> edges;
    public final double totalWeight;
    public final boolean connected;

    public MSTResult(List<Edge> edges, double totalWeight, boolean connected) {
        this.edges = edges;
        this.totalWeight = totalWeight;
        this.connected = connected;
    }
}