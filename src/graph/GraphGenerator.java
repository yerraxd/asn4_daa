package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GraphGenerator {

    public static Graph connectedUndirected(int n, int edges, long seed) {
        Random random = new Random(seed);
        Graph graph = new Graph(n, false);

        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            order.add(i);
        }
        Collections.shuffle(order, random);

        for (int i = 1; i < n; i++) {
            int parent = order.get(random.nextInt(i));
            graph.addEdge(parent, order.get(i), randomWeight(random));
        }
        return graph;
    }

    private static int randomWeight(Random random) {
        return 1 + random.nextInt(100);
    }
}