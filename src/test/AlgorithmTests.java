package test;

import connectivity.SCCResult;
import connectivity.TarjanSCC;
import connectivity.TopoResult;
import connectivity.TopologicalSort;
import graph.Edge;
import graph.Graph;
import mst.Kruskal;
import mst.MSTResult;
import mst.Prim;
import shortestpath.Dijkstra;
import shortestpath.PathResult;
import java.util.List;
import graph.GraphGenerator;
import java.util.Arrays;
import java.util.Random;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AlgorithmTests {
    private static int passed = 0;
    private static int failed = 0;
    private static final double EPS = 1e-9;
    private static final List<String> rows = new ArrayList<>();

    static void check(String name, boolean ok) {
        if (ok) {
            passed++;
            System.out.println("PASS  " + name);
        } else {
            failed++;
            System.out.println("FAIL  " + name);
        }
        rows.add(rows.size() + 1 + ",\"" + name.replace("\"", "\"\"") + "\"," + (ok ? "PASS" : "FAIL"));
    }
    static Graph build(int n, boolean directed, double[][] edges) {
        Graph g = new Graph(n, directed);
        for (double[] e : edges) {
            g.addEdge((int) e[0], (int) e[1], e[2]);
        }
        return g;
    }

    static boolean same(double a, double b) {
        return Math.abs(a - b) < EPS;
    }

    public static void main(String[] args) throws IOException {
        edgeCaseTests();
        randomTests();

        System.out.println("\n" + passed + " passed, " + failed + " failed");
        writeTable();
        if (failed > 0) {
            System.exit(1);
        }
    }

    static void writeTable() throws IOException {
        List<String> out = new ArrayList<>();
        out.add("id,check,result");
        out.addAll(rows);
        out.add("total," + (passed + failed) + " checks," + passed + " passed / " + failed + " failed");
        Path file = Paths.get("results", "tables", "tests.csv");
        Files.createDirectories(file.getParent());
        Files.write(file, out);
    }

    static void edgeCaseTests() {
        Graph small = build(4, false, new double[][]{
                {0, 1, 1}, {1, 2, 2}, {2, 3, 3}, {0, 3, 10}, {0, 2, 4}});
        MSTResult k = new Kruskal().run(small);
        MSTResult p = new Prim().run(small);
        check("small: Kruskal weight = 6", same(k.totalWeight, 6));
        check("small: Prim weight = 6", same(p.totalWeight, 6));
        check("small: MST has V-1 edges", k.edges.size() == 3 && p.edges.size() == 3);
        check("small: connected flag", k.connected && p.connected);

        Graph disc = build(5, false, new double[][]{
                {0, 1, 1}, {1, 2, 2}, {0, 2, 5}, {3, 4, 7}});
        k = new Kruskal().run(disc);
        p = new Prim().run(disc);
        check("disconnected: Kruskal forest weight = 10", same(k.totalWeight, 10));
        check("disconnected: Prim forest weight = 10", same(p.totalWeight, 10));
        check("disconnected: forest has V-2 edges", k.edges.size() == 3 && p.edges.size() == 3);
        check("disconnected: connected = false", !k.connected && !p.connected);

        Graph multi = build(4, false, new double[][]{
                {0, 1, 1}, {1, 2, 1}, {2, 3, 1}, {3, 0, 1}});
        k = new Kruskal().run(multi);
        p = new Prim().run(multi);
        check("multiple MST: Kruskal weight = 3", same(k.totalWeight, 3));
        check("multiple MST: weights equal", same(k.totalWeight, p.totalWeight));

        Graph k5 = new Graph(5, false);
        for (int i = 0; i < 5; i++) {
            for (int j = i + 1; j < 5; j++) {
                k5.addEdge(i, j, 1);
            }
        }
        k = new Kruskal().run(k5);
        p = new Prim().run(k5);
        check("duplicate weights K5: Kruskal = 4", same(k.totalWeight, 4));
        check("duplicate weights K5: Prim = 4", same(p.totalWeight, 4));

        Graph one = new Graph(1, false);
        Graph oneDir = new Graph(1, true);
        k = new Kruskal().run(one);
        p = new Prim().run(one);
        check("single vertex: Kruskal weight 0, no edges, connected", same(k.totalWeight, 0) && k.edges.isEmpty() && k.connected);
        check("single vertex: Prim weight 0, no edges, connected", same(p.totalWeight, 0) && p.edges.isEmpty() && p.connected);
        TopoResult t1 = new TopologicalSort().run(oneDir);
        check("single vertex: topo order =, no cycle", !t1.hasCycle && t1.order.equals(List.of(0)));
        SCCResult s1 = new TarjanSCC().run(oneDir);
        check("single vertex: one SCC", s1.components.size() == 1);
        PathResult d1 = new Dijkstra().run(one, 0);
        check("single vertex: dist[0] = 0", d1.distances[0] == 0);

        Graph cyc = build(3, true, new double[][]{{0, 1, 1}, {1, 2, 1}, {2, 0, 1}});
        TopoResult tc = new TopologicalSort().run(cyc);
        check("cycle: detected by topological sort", tc.hasCycle);
        check("cycle: no order returned", tc.order.isEmpty());

        Graph dag = build(6, true, new double[][]{
                {5, 2, 1}, {5, 0, 1}, {4, 0, 1}, {4, 1, 1}, {2, 3, 1}, {3, 1, 1}});
        TopologicalSort sorter = new TopologicalSort();
        TopoResult td = sorter.run(dag);
        check("DAG: no cycle", !td.hasCycle);
        check("DAG: order is valid", sorter.isValidOrder(dag, td.order));

        Graph sccG = build(4, true, new double[][]{
                {0, 1, 1}, {1, 2, 1}, {2, 0, 1}, {2, 3, 1}});
        TarjanSCC tarjan = new TarjanSCC();
        SCCResult sr = tarjan.run(sccG);
        int[] c = sr.componentOf;
        check("SCC: valid partition", tarjan.isValidPartition(sccG, sr));
        check("SCC: 2 components", sr.components.size() == 2);
        check("SCC: {0,1,2} together, 3 apart", c[0] == c[1] && c[1] == c[2] && c[3] != c[0]);
        SCCResult dagScc = tarjan.run(dag);
        check("SCC on DAG: every vertex is its own component", dagScc.components.size() == dag.getVertexCount());

        Graph unr = build(4, false, new double[][]{{0, 1, 5}, {1, 2, 3}});
        PathResult pr = new Dijkstra().run(unr, 0);
        check("unreachable: dist[2] = 8", same(pr.distances[2], 8));
        check("unreachable: vertex 3 is unreachable", !pr.isReachable(3));
        check("unreachable: dist[3] = infinity", pr.distances[3] == Double.POSITIVE_INFINITY);
        check("unreachable: path to 3 is empty", pr.getPath(3).isEmpty());

        Graph road = build(6, false, new double[][]{
                {0, 1, 7}, {0, 2, 9}, {0, 5, 14}, {1, 2, 10}, {1, 3, 15}, {2, 3, 11}, {2, 5, 2}, {3, 4, 6}, {4, 5, 9}});
        Dijkstra dj = new Dijkstra();
        PathResult rr = dj.run(road, 0);
        check("Dijkstra: dist to 4 = 20", same(rr.distances[4], 20));
        check("Dijkstra: path to 4 = [0, 2, 5, 4]", rr.getPath(4).equals(List.of(0, 2, 5, 4)));
        check("Dijkstra: result passes isValid", dj.isValid(road, rr));

        Graph neg = build(3, true, new double[][]{{0, 1, 4}, {0, 2, 5}, {2, 1, -3}});
        boolean threw = false;
        try {
            new Dijkstra().run(neg, 0);
        } catch (IllegalArgumentException ex) {
            threw = true;
        }
        check("Dijkstra: negative weight rejected", threw);
    }
    static final int RUNS = 100;

    static void randomTests() {
        boolean mstOk = true;
        for (int seed = 0; seed < RUNS; seed++) {
            Random r = new Random(seed);
            int n = 2 + r.nextInt(60);
            int maxE = n * (n - 1) / 2;
            int m = n - 1 + r.nextInt(Math.min(3 * n, maxE - (n - 1)) + 1);
            Graph g = GraphGenerator.connectedUndirected(n, m, seed);
            MSTResult k = new Kruskal().run(g);
            MSTResult p = new Prim().run(g);
            if (!same(k.totalWeight, p.totalWeight) || !k.connected || !p.connected
                    || k.edges.size() != n - 1 || p.edges.size() != n - 1) {
                mstOk = false;
                System.out.println("  MST mismatch, seed=" + seed);
            }
        }
        check("random connected: Kruskal weight == Prim weight, V-1 edges", mstOk);

        boolean dupOk = true;
        for (int seed = 0; seed < RUNS; seed++) {
            Random r = new Random(seed);
            int n = 2 + r.nextInt(60);
            int maxE = n * (n - 1) / 2;
            int m = n - 1 + r.nextInt(Math.min(3 * n, maxE - (n - 1)) + 1);
            Graph base = GraphGenerator.connectedUndirected(n, m, seed);
            Graph g = new Graph(n, false);
            for (Edge e : base.getEdges()) {
                g.addEdge(e.from, e.to, 1 + ((int) e.weight % 3));
            }
            if (!same(new Kruskal().run(g).totalWeight, new Prim().run(g).totalWeight)) {
                dupOk = false;
                System.out.println("  duplicate-weights mismatch, seed=" + seed);
            }
        }
        check("random with duplicate weights: Kruskal == Prim", dupOk);

        boolean dagOk = true;
        TopologicalSort sorter = new TopologicalSort();
        for (int seed = 0; seed < RUNS; seed++) {
            Random r = new Random(seed);
            int n = 2 + r.nextInt(60);
            int m = r.nextInt(Math.min(3 * n, n * (n - 1) / 2) + 1);
            Graph g = GraphGenerator.dag(n, m, seed);
            TopoResult t = sorter.run(g);
            if (t.hasCycle || !sorter.isValidOrder(g, t.order)) {
                dagOk = false;
                System.out.println("  bad topological order, seed=" + seed);
            }
        }
        check("random DAGs: valid topological order, no false cycle", dagOk);

        boolean partitionOk = true;
        boolean cycleOk = true;
        TarjanSCC tarjan = new TarjanSCC();
        for (int seed = 0; seed < RUNS; seed++) {
            Random r = new Random(seed);
            int n = 2 + r.nextInt(40);
            int m = r.nextInt(Math.min(3 * n, n * (n - 1)) + 1);
            Graph g = GraphGenerator.directed(n, m, seed);
            SCCResult s = tarjan.run(g);
            if (!tarjan.isValidPartition(g, s)) {
                partitionOk = false;
            }
            boolean hasBigScc = s.components.size() < n;
            if (sorter.run(g).hasCycle != hasBigScc) {
                cycleOk = false;
                System.out.println("  cycle/SCC disagreement, seed=" + seed);
            }
        }
        check("random directed: every vertex in exactly one SCC", partitionOk);
        check("random directed: cycle detection agrees with SCC", cycleOk);

        boolean dijOk = true;
        boolean validOk = true;
        for (int seed = 0; seed < RUNS; seed++) {
            Random r = new Random(seed);
            int n = 2 + r.nextInt(50);
            Graph g;
            if (seed % 2 == 0) {
                int m = r.nextInt(Math.min(3 * n, n * (n - 1)) + 1);
                g = GraphGenerator.directed(n, m, seed);
            } else {
                int maxE = n * (n - 1) / 2;
                int m = n - 1 + r.nextInt(Math.min(3 * n, maxE - (n - 1)) + 1);
                g = GraphGenerator.connectedUndirected(n, m, seed);
            }
            Dijkstra dj = new Dijkstra();
            PathResult res = dj.run(g, 0);
            double[] ref = bellmanFord(g, 0);
            for (int v = 0; v < n; v++) {
                boolean bothInf = Double.isInfinite(res.distances[v]) && Double.isInfinite(ref[v]);
                if (!bothInf && !same(res.distances[v], ref[v])) {
                    dijOk = false;
                    System.out.println("  Dijkstra mismatch, seed=" + seed + " vertex=" + v);
                    break;
                }
            }
            if (!dj.isValid(g, res)) {
                validOk = false;
            }
        }
        check("random graphs: Dijkstra == Bellman-Ford (incl. unreachable)", dijOk);
        check("random graphs: Dijkstra result passes isValid", validOk);
    }

    static double[] bellmanFord(Graph g, int source) {
        int n = g.getVertexCount();
        double[] d = new double[n];
        Arrays.fill(d, Double.POSITIVE_INFINITY);
        d[source] = 0;
        for (int i = 0; i < n; i++) {
            for (Edge e : g.getEdges()) {
                if (d[e.from] + e.weight < d[e.to]) {
                    d[e.to] = d[e.from] + e.weight;
                }
                if (!g.isDirected() && d[e.to] + e.weight < d[e.from]) {
                    d[e.from] = d[e.to] + e.weight;
                }
            }
        }
        return d;
    }

}
