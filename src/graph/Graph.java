package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int vertices;
    private final List<Edge> edges = new ArrayList<>();

    public Graph(int vertices) {
        this.vertices = vertices;
    }

    public void addEdge(int from, int to, double weight) {
        edges.add(new Edge(from, to, weight));
    }

    public int getVertexCount() {
        return vertices;
    }

    public int getEdgeCount() {
        return edges.size();
    }

    public List<Edge> getEdges() {
        return edges;
    }
}