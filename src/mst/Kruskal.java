package mst;

import dsu.DSU;
import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Kruskal {

    public List<Edge> run(Graph graph) {
        List<Edge> sorted = new ArrayList<>(graph.getEdges());
        sorted.sort(Comparator.comparingDouble(e -> e.weight));

        DSU dsu = new DSU(graph.getVertexCount());
        List<Edge> result = new ArrayList<>();

        for (Edge e : sorted) {
            if (dsu.union(e.from, e.to)) {
                result.add(e);
            }
        }
        return result;
    }
}