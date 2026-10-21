package mst;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Prim {

    public MSTResult run(Graph graph) {
        int n = graph.getVertexCount();
        boolean[] visited = new boolean[n];
        List<Edge> result = new ArrayList<>();
        double total = 0;

        PriorityQueue<Edge> queue = new PriorityQueue<>((a, b) -> Double.compare(a.weight, b.weight));

        if (n > 0) {
            visited[0] = true;
            queue.addAll(graph.getAdjacent(0));
        }

        while (!queue.isEmpty()) {
            Edge e = queue.poll();
            if (visited[e.to]) {
                continue;
            }
            visited[e.to] = true;
            result.add(e);
            total += e.weight;
            queue.addAll(graph.getAdjacent(e.to));
        }

        boolean connected = n == 0 || result.size() == n - 1;
        return new MSTResult(result, total, connected, 0, 0, 0, 0, 0, 0, 0);
    }
}