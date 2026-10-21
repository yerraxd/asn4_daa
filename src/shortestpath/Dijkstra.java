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

        PriorityQueue<double[]> queue = new PriorityQueue<>((a, b) -> Double.compare(a[0], b[0]));
        queue.add(new double[]{0, source});

        while (!queue.isEmpty()) {
            double[] top = queue.poll();
            int u = (int) top[1];
            if (top[0] > dist[u]) {
                continue;
            }
            for (Edge e : graph.getAdjacent(u)) {
                double candidate = dist[u] + e.weight;
                if (candidate < dist[e.to]) {
                    dist[e.to] = candidate;
                    parent[e.to] = u;
                    queue.add(new double[]{candidate, e.to});
                }
            }
        }
        return new PathResult(source, dist, parent);
    }
}