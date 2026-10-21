package benchmark;

import graph.Graph;
import graph.GraphGenerator;
import mst.Kruskal;
import mst.MSTResult;

public class Benchmark {
    private static final long SEED = 42;
    private static final int REPEATS = 5;
    private static final int[][] SIZES = {{10, 30}, {100, 500}, {1000, 5000}};

    public static void main(String[] args) {
        for (int[] size : SIZES) {
            Graph graph = GraphGenerator.connectedUndirected(size[0], size[1], SEED);
            long total = 0;
            MSTResult last = null;
            for (int i = 0; i < REPEATS; i++) {
                last = new Kruskal().run(graph);
                total += last.timeNanos;
            }
            System.out.println("Kruskal V=" + size[0] + " E=" + size[1]
                    + " avgNs=" + total / REPEATS + " examined=" + last.edgesExamined);
        }
    }
}