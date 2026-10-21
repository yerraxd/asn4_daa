package mst;

import dsu.DSU;
import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Kruskal {

    public MSTResult run(Graph graph) {
        int n = graph.getVertexCount();
        List<Edge> sorted = new ArrayList<>(graph.getEdges());
        sorted.sort(Comparator.comparingDouble(e -> e.weight));

        DSU dsu = new DSU(n);
        List<Edge> result = new ArrayList<>();
        double total = 0;

        for (Edge e : sorted) {
            if (dsu.union(e.from, e.to)) {
                result.add(e);
                total += e.weight;
                if (result.size() == n - 1) {
                    break;
                }
            }
        }

        boolean connected = n == 0 || result.size() == n - 1;
        return new MSTResult(result, total, connected);
    }
}