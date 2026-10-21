package connectivity;

import graph.Edge;
import graph.Graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
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

    public boolean isValidOrder(Graph graph, List<Integer> order) {
        int n = graph.getVertexCount();
        if (order.size() != n) {
            return false;
        }

        int[] position = new int[n];
        Arrays.fill(position, -1);
        for (int i = 0; i < n; i++) {
            int v = order.get(i);
            if (v < 0 || v >= n || position[v] != -1) {
                return false;
            }
            position[v] = i;
        }

        for (Edge e : graph.getEdges()) {
            if (position[e.from] >= position[e.to]) {
                return false;
            }
        }
        return true;
    }
}