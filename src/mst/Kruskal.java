package mst;

import dsu.DSU;
import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.List;

public class Kruskal {

    public MSTResult run(Graph graph) {
        long start = System.nanoTime();

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
                if (dsu.getComponents() == 1) {
                    break;
                }
            }
        }

        boolean connected = dsu.getComponents() <= 1;
        long time = System.nanoTime() - start;

        return new MSTResult(result, total, connected, examined, comparisons[0],
                dsu.getFindCalls(), dsu.getUnionCalls(), 0, 0, time);
    }
}