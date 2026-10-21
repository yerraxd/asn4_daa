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
        int components = 0;

        PriorityQueue<Edge> queue = new PriorityQueue<>((a, b) -> Double.compare(a.weight, b.weight));

        for (int s = 0; s < n; s++) {
            if (visited[s]) {
                continue;
            }
            components++;
            visited[s] = true;
            queue.addAll(graph.getAdjacent(s));

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
        }

        boolean connected = components <= 1;
        return new MSTResult(result, total, connected, 0, 0, 0, 0, 0, 0, 0);
    }
}