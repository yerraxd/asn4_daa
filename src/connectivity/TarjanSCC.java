package connectivity;

import graph.Edge;
import graph.Graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class TarjanSCC {
    private Graph graph;
    private int[] index;
    private int[] lowLink;
    private int[] componentOf;
    private boolean[] onStack;
    private Deque<Integer> stack;
    private List<List<Integer>> components;
    private int counter;
    private long examined;
    private long dfsCalls;

    public SCCResult run(Graph graph) {
        long startTime = System.nanoTime();

        this.graph = graph;
        int n = graph.getVertexCount();
        index = new int[n];
        lowLink = new int[n];
        componentOf = new int[n];
        onStack = new boolean[n];
        stack = new ArrayDeque<>();
        components = new ArrayList<>();
        counter = 0;
        examined = 0;
        dfsCalls = 0;
        Arrays.fill(index, -1);

        for (int v = 0; v < n; v++) {
            if (index[v] == -1) {
                dfs(v);
            }
        }

        long time = System.nanoTime() - startTime;
        return new SCCResult(components, componentOf, examined, dfsCalls, time);
    }

    private void dfs(int v) {
        dfsCalls++;
        index[v] = counter;
        lowLink[v] = counter;
        counter++;
        stack.push(v);
        onStack[v] = true;

        for (Edge e : graph.getAdjacent(v)) {
            examined++;
            int w = e.to;
            if (index[w] == -1) {
                dfs(w);
                lowLink[v] = Math.min(lowLink[v], lowLink[w]);
            } else if (onStack[w]) {
                lowLink[v] = Math.min(lowLink[v], index[w]);
            }
        }

        if (lowLink[v] == index[v]) {
            List<Integer> component = new ArrayList<>();
            int id = components.size();
            int w;
            do {
                w = stack.pop();
                onStack[w] = false;
                componentOf[w] = id;
                component.add(w);
            } while (w != v);
            components.add(component);
        }
    }

    public boolean isValidPartition(Graph graph, SCCResult result) {
        int n = graph.getVertexCount();
        int[] seen = new int[n];
        int total = 0;
        for (List<Integer> component : result.components) {
            for (int v : component) {
                seen[v]++;
                total++;
            }
        }
        if (total != n) {
            return false;
        }
        for (int v = 0; v < n; v++) {
            if (seen[v] != 1) {
                return false;
            }
        }
        return true;
    }
}