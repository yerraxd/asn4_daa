package mst;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Prim {
    private long examined;
    private long comparisons;
    private long pushes;
    private long pops;

    public MSTResult run(Graph graph) {
        if (graph.isDirected()) {
            throw new IllegalArgumentException("Prim requires an undirected graph");
        }

        long startTime = System.nanoTime();
        examined = 0;
        comparisons = 0;
        pushes = 0;
        pops = 0;

        int n = graph.getVertexCount();
        boolean[] visited = new boolean[n];
        List<Edge> result = new ArrayList<>();
        double total = 0;
        int components = 0;

        PriorityQueue<Edge> queue = new PriorityQueue<>((a, b) -> {
            comparisons++;
            return Double.compare(a.weight, b.weight);
        });

        for (int s = 0; s < n; s++) {
            if (visited[s]) {
                continue;
            }
            components++;
            visited[s] = true;
            push(queue, graph, s);

            while (!queue.isEmpty()) {
                Edge e = queue.poll();
                pops++;
                examined++;
                if (visited[e.to]) {
                    continue;
                }
                visited[e.to] = true;
                result.add(e);
                total += e.weight;
                push(queue, graph, e.to);
            }
        }

        boolean connected = components <= 1;
        long time = System.nanoTime() - startTime;
        return new MSTResult(result, total, connected, examined, comparisons, 0, 0, pushes, pops, time);
    }

    private void push(PriorityQueue<Edge> queue, Graph graph, int v) {
        for (Edge next : graph.getAdjacent(v)) {
            queue.add(next);
            pushes++;
        }
    }
}