package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int vertices;
    private final boolean directed;
    private final List<Edge> edges = new ArrayList<>();
    private final List<List<Edge>> adjacency = new ArrayList<>();

    public Graph(int vertices, boolean directed) {
        this.vertices = vertices;
        this.directed = directed;
        for (int i = 0; i < vertices; i++) {
            adjacency.add(new ArrayList<>());
        }
    }

    public void addEdge(int from, int to, double weight) {
        checkVertex(from);
        checkVertex(to);
        Edge edge = new Edge(from, to, weight);
        edges.add(edge);
        adjacency.get(from).add(edge);
        if (!directed) {
            adjacency.get(to).add(new Edge(to, from, weight));
        }
    }

    private void checkVertex(int v) {
        if (v < 0 || v >= vertices) {
            throw new IllegalArgumentException("Invalid vertex: " + v);
        }
    }

    public int getVertexCount() {
        return vertices;
    }

    public int getEdgeCount() {
        return edges.size();
    }

    public boolean isDirected() {
        return directed;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Edge> getAdjacent(int v) {
        checkVertex(v);
        return adjacency.get(v);
    }
}