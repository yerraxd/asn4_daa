package connectivity;

import graph.Edge;
import graph.Graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TopologicalSort {

    public TopoResult run(Graph graph) {
        int n = graph.getVertexCount();
        int[] inDegree = new int[n];
        for (Edge e : graph.getEdges()) {
            inDegree[e.to]++;
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int v = 0; v < n; v++) {
            if (inDegree[v] == 0) {
                queue.add(v);
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int v = queue.poll();
            order.add(v);
            for (Edge e : graph.getAdjacent(v)) {
                inDegree[e.to]--;
                if (inDegree[e.to] == 0) {
                    queue.add(e.to);
                }
            }
        }

        if (order.size() != n) {
            return new TopoResult(new ArrayList<>(), true);
        }
        return new TopoResult(order, false);
    }
}