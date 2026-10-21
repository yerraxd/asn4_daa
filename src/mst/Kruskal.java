package mst;

import dsu.DSU;
import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.List;

public class Kruskal {

    public MSTResult run(Graph graph) {
        int n = graph.getVertexCount();
        List<Edge> sorted = new ArrayList<>(graph.getEdges());

        long[] comparisons = {0};
        sorted.sort((a, b) -> {
            comparisons[0]++;
            return Double.compare(a.weight, b.weight);
        });

        DSU dsu = new DSU(n);
        List<Edge> result = new ArrayList<>();
        double total = 0;
        long examined = 0;

        for (Edge e : sorted) {
            examined++;
            if (dsu.union(e.from, e.to)) {
                result.add(e);
                total += e.weight;
                if (result.size() == n - 1) {
                    break;
                }
            }
        }

        boolean connected = n == 0 || result.size() == n - 1;
        return new MSTResult(result, total, connected, examined, comparisons[0]);
    }
}