package shortestpath;

import graph.Edge;
import graph.Graph;

import java.util.Arrays;
import java.util.PriorityQueue;

public class Dijkstra {

    public PathResult run(Graph graph, int source) {
        int n = graph.getVertexCount();
        double[] dist = new double[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);
        dist[source] = 0;

        PriorityQueue<Node> queue = new PriorityQueue<>((a, b) -> Double.compare(a.dist, b.dist));
        queue.add(new Node(source, 0));

        while (!queue.isEmpty()) {
            Node top = queue.poll();
            int u = top.vertex;
            if (top.dist > dist[u]) {
                continue;
            }
            for (Edge e : graph.getAdjacent(u)) {
                double candidate = dist[u] + e.weight;
                if (candidate < dist[e.to]) {
                    dist[e.to] = candidate;
                    parent[e.to] = u;
                    queue.add(new Node(e.to, candidate));
                }
            }
        }
        return new PathResult(source, dist, parent);
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