package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GraphGenerator {

    public static Graph connectedUndirected(int n, int edges, long seed) {
        if (n < 1) {
            throw new IllegalArgumentException("Vertex count must be positive");
        }
        long maxEdges = (long) n * (n - 1) / 2;
        if (edges < n - 1 || edges > maxEdges) {
            throw new IllegalArgumentException("Edge count must be between " + (n - 1) + " and " + maxEdges);
        }

        Random random = new Random(seed);
        Graph graph = new Graph(n, false);
        Set<Long> used = new HashSet<>();

        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            order.add(i);
        }
        Collections.shuffle(order, random);

        for (int i = 1; i < n; i++) {
            int a = order.get(random.nextInt(i));
            int b = order.get(i);
            used.add(undirectedKey(a, b, n));
            graph.addEdge(a, b, randomWeight(random));
        }

        int added = n - 1;
        while (added < edges) {
            int a = random.nextInt(n);
            int b = random.nextInt(n);
            if (a == b || !used.add(undirectedKey(a, b, n))) {
                continue;
            }
            graph.addEdge(a, b, randomWeight(random));
            added++;
        }
        return graph;
    }

    private static long undirectedKey(int a, int b, int n) {
        return (long) Math.min(a, b) * n + Math.max(a, b);
    }

    private static int randomWeight(Random random) {
        return 1 + random.nextInt(100);
    }
}