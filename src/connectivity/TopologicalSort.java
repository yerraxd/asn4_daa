package connectivity;

import graph.Edge;
import graph.Graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TopologicalSort {

    public TopoResult run(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException("Topological sort requires a directed graph");
        }

        long start = System.nanoTime();

        int n = graph.getVertexCount();
        long examined = 0;
        long pushes = 0;
        long pops = 0;

        int[] inDegree = new int[n];
        for (Edge e : graph.getEdges()) {
            inDegree[e.to]++;
            examined++;
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int v = 0; v < n; v++) {
            if (inDegree[v] == 0) {
                queue.add(v);
                pushes++;
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int v = queue.poll();
            pops++;
            order.add(v);
            for (Edge e : graph.getAdjacent(v)) {
                examined++;
                inDegree[e.to]--;
                if (inDegree[e.to] == 0) {
                    queue.add(e.to);
                    pushes++;
                }
            }
        }

        long time = System.nanoTime() - start;
        if (order.size() != n) {
            return new TopoResult(new ArrayList<>(), true, examined, pushes, pops, time);
        }
        return new TopoResult(order, false, examined, pushes, pops, time);
    }
}