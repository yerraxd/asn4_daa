package shortestpath;

import graph.Edge;
import graph.Graph;

import java.util.Arrays;
import java.util.PriorityQueue;

public class Dijkstra {
    private long examined;
    private long relaxations;
    private long pushes;
    private long pops;
    private long comparisons;

    public PathResult run(Graph graph, int source) {
        long startTime = System.nanoTime();

        int n = graph.getVertexCount();
        if (source < 0 || source >= n) {
            throw new IllegalArgumentException("Invalid source vertex: " + source);
        }
        for (Edge e : graph.getEdges()) {
            if (e.weight < 0) {
                throw new IllegalArgumentException("Dijkstra requires non-negative edge weights");
            }
        }

        examined = 0;
        relaxations = 0;
        pushes = 0;
        pops = 0;
        comparisons = 0;

        double[] dist = new double[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);
        dist[source] = 0;

        PriorityQueue<Node> queue = new PriorityQueue<>((a, b) -> {
            comparisons++;
            return Double.compare(a.dist, b.dist);
        });
        queue.add(new Node(source, 0));
        pushes++;

        while (!queue.isEmpty()) {
            Node top = queue.poll();
            pops++;
            int u = top.vertex;
            if (top.dist > dist[u]) {
                continue;
            }
            for (Edge e : graph.getAdjacent(u)) {
                examined++;
                double candidate = dist[u] + e.weight;
                if (candidate < dist[e.to]) {
                    dist[e.to] = candidate;
                    parent[e.to] = u;
                    relaxations++;
                    queue.add(new Node(e.to, candidate));
                    pushes++;
                }
            }
        }

        long time = System.nanoTime() - startTime;
        return new PathResult(source, dist, parent, examined, relaxations,
                pushes, pops, comparisons, time);
    }

    public boolean isValid(Graph graph, PathResult r) {
        int n = graph.getVertexCount();
        if (r.distances[r.source] != 0) {
            return false;
        }

        for (int u = 0; u < n; u++) {
            if (!r.isReachable(u)) {
                continue;
            }
            for (Edge e : graph.getAdjacent(u)) {
                if (r.distances[e.to] > r.distances[u] + e.weight + 1e-9) {
                    return false;
                }
            }
        }

        for (int v = 0; v < n; v++) {
            if (v == r.source || !r.isReachable(v)) {
                continue;
            }
            int p = r.parents[v];
            if (p == -1) {
                return false;
            }
            boolean found = false;
            for (Edge e : graph.getAdjacent(p)) {
                if (e.to == v && Math.abs(r.distances[p] + e.weight - r.distances[v]) < 1e-9) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    private static class Node {
        final int vertex;
        final double dist;

        Node(int vertex, double dist) {
            this.vertex = vertex;
            this.dist = dist;
        }
    }
}