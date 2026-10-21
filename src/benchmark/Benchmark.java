package benchmark;

import graph.Graph;
import graph.GraphGenerator;
import mst.Kruskal;
import mst.MSTResult;
import mst.Prim;

public class Benchmark {
    private static final long SEED = 42;
    private static final int REPEATS = 5;
    private static final int[][] SIZES = {{10, 30}, {100, 500}, {1000, 5000}};

    private interface Run {
        Counters run();
    }

    private static class Counters {
        long time;
        long examined;
        long comparisons;
        long find;
        long union;
        long pushes;
        long pops;
        long dfs;
        long relaxations;
    }

    public static void main(String[] args) {
        for (int[] size : SIZES) {
            int n = size[0];
            int m = size[1];
            Graph undirected = GraphGenerator.connectedUndirected(n, m, SEED);

            measure("Kruskal", "undirected", n, m, () -> kruskal(undirected));
            measure("Prim", "undirected", n, m, () -> prim(undirected));
        }
    }
    private static void measure(String algorithm, String graphType, int n, int m, Run run) {
        long total = 0;
        Counters last = null;
        for (int i = 0; i < REPEATS; i++) {
            last = run.run();
            total += last.time;
        }
        System.out.println(algorithm + " " + graphType + " V=" + n + " E=" + m
                + " avgNs=" + total / REPEATS + " examined=" + last.examined);
    }

    private static Counters kruskal(Graph g) {
        MSTResult r = new Kruskal().run(g);
        Counters c = new Counters();
        c.time = r.timeNanos;
        c.examined = r.edgesExamined;
        c.comparisons = r.comparisons;
        c.find = r.findOps;
        c.union = r.unionOps;
        return c;
    }
    private static Counters prim(Graph g) {
        MSTResult r = new Prim().run(g);
        Counters c = new Counters();
        c.time = r.timeNanos;
        c.examined = r.edgesExamined;
        c.comparisons = r.comparisons;
        c.pushes = r.pqPushes;
        c.pops = r.pqPops;
        return c;
    }
}