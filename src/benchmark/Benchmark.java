package benchmark;

import graph.Graph;
import graph.GraphGenerator;
import mst.Kruskal;
import mst.MSTResult;
import mst.Prim;
import connectivity.SCCResult;
import connectivity.TarjanSCC;
import connectivity.TopoResult;
import connectivity.TopologicalSort;
import shortestpath.Dijkstra;
import shortestpath.PathResult;

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
            Graph directed = GraphGenerator.directed(n, m, SEED);
            Graph dag = GraphGenerator.dag(n, m, SEED);

            measure("Kruskal", "undirected", n, m, () -> kruskal(undirected));
            measure("Prim", "undirected", n, m, () -> prim(undirected));
            measure("TopologicalSort", "dag", n, m, () -> topo(dag));
            measure("TopologicalSort", "cyclic", n, m, () -> topo(directed));
            measure("Tarjan", "directed", n, m, () -> scc(directed));
            measure("Tarjan", "dag", n, m, () -> scc(dag));
            measure("Dijkstra", "undirected", n, m, () -> dijkstra(undirected));
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

    private static Counters topo(Graph g) {
        TopoResult r = new TopologicalSort().run(g);
        Counters c = new Counters();
        c.time = r.timeNanos;
        c.examined = r.edgesExamined;
        c.pushes = r.queuePushes;
        c.pops = r.queuePops;
        return c;
    }

    private static Counters scc(Graph g) {
        SCCResult r = new TarjanSCC().run(g);
        Counters c = new Counters();
        c.time = r.timeNanos;
        c.examined = r.edgesExamined;
        c.dfs = r.dfsCalls;
        return c;
    }
    private static Counters dijkstra(Graph g) {
        PathResult r = new Dijkstra().run(g, 0);
        Counters c = new Counters();
        c.time = r.timeNanos;
        c.examined = r.edgesExamined;
        c.comparisons = r.comparisons;
        c.pushes = r.pqPushes;
        c.pops = r.pqPops;
        c.relaxations = r.relaxations;
        return c;
    }
}