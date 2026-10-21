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

public class AlgorithmTests {
    private static int passed = 0;
    private static int failed = 0;
    private static final double EPS = 1e-9;

    static void check(String name, boolean ok) {
        if (ok) {
            passed++;
            System.out.println("PASS " + name);
        } else {
            failed++;
            System.out.println("FAIL " + name);
        }
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

    public static void main(String[] args) {
        edgeCaseTests();
        System.out.println("\n" + passed + " passed, " + failed + " failed");
        if (failed > 0) {
            System.exit(1);
        }
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
}
