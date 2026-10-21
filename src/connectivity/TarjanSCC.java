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
    private boolean[] onStack;
    private Deque<Integer> stack;
    private List<List<Integer>> components;
    private int counter;

    public SCCResult run(Graph graph) {
        this.graph = graph;
        int n = graph.getVertexCount();
        index = new int[n];
        lowLink = new int[n];
        onStack = new boolean[n];
        stack = new ArrayDeque<>();
        components = new ArrayList<>();
        counter = 0;
        Arrays.fill(index, -1);

        for (int v = 0; v < n; v++) {
            if (index[v] == -1) {
                dfs(v);
            }
        }
        return new SCCResult(components);
    }

    private void dfs(int v) {
        index[v] = counter;
        lowLink[v] = counter;
        counter++;
        stack.push(v);
        onStack[v] = true;

        for (Edge e : graph.getAdjacent(v)) {
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
            int w;
            do {
                w = stack.pop();
                onStack[w] = false;
                component.add(w);
            } while (w != v);
            components.add(component);
        }
    }
}