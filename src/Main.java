import connectivity.SCCResult;
import connectivity.TarjanSCC;
import connectivity.TopoResult;
import connectivity.TopologicalSort;
import graph.Edge;
import graph.Graph;
import mst.Kruskal;
import mst.MSTResult;
import mst.Prim;

public class Main {
    public static void main(String[] args) {
        Graph g = new Graph(6, false);
        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 3);
        g.addEdge(1, 2, 1);
        g.addEdge(1, 3, 2);
        g.addEdge(2, 3, 4);
        g.addEdge(3, 4, 2);
        g.addEdge(4, 5, 6);
        compare("Connected graph", g);

        Graph d = new Graph(5, false);
        d.addEdge(0, 1, 1);
        d.addEdge(1, 2, 2);
        d.addEdge(3, 4, 5);
        compare("Disconnected graph", d);

        Graph dag = new Graph(6, true);
        dag.addEdge(5, 2, 1);
        dag.addEdge(5, 0, 1);
        dag.addEdge(4, 0, 1);
        dag.addEdge(4, 1, 1);
        dag.addEdge(2, 3, 1);
        dag.addEdge(3, 1, 1);
        topo("DAG", dag);

        Graph cyclic = new Graph(4, true);
        cyclic.addEdge(0, 1, 1);
        cyclic.addEdge(1, 2, 1);
        cyclic.addEdge(2, 0, 1);
        cyclic.addEdge(2, 3, 1);
        topo("Graph with cycle", cyclic);

        Graph city = new Graph(8, true);
        city.addEdge(0, 1, 1);
        city.addEdge(1, 2, 1);
        city.addEdge(2, 0, 1);
        city.addEdge(2, 3, 1);
        city.addEdge(3, 4, 1);
        city.addEdge(4, 5, 1);
        city.addEdge(5, 3, 1);
        city.addEdge(6, 5, 1);
        city.addEdge(6, 7, 1);
        city.addEdge(7, 6, 1);
        scc("City with one-way roads", city);
        scc("SCC on DAG", dag);
        scc("SCC on graph with cycle", cyclic);
    }

    private static void compare(String name, Graph graph) {
        MSTResult k = new Kruskal().run(graph);
        MSTResult p = new Prim().run(graph);

        System.out.println("=== " + name + " ===");
        print("Kruskal", k);
        print("Prim", p);
        System.out.println("Same weight: " + (Math.abs(k.totalWeight - p.totalWeight) < 1e-9));
        System.out.println();
    }

    private static void print(String name, MSTResult r) {
        System.out.println(name + ": weight=" + r.totalWeight + " connected=" + r.connected);
        for (Edge e : r.edges) {
            System.out.println("  " + e);
        }
        System.out.println("  examined=" + r.edgesExamined + " cmp=" + r.comparisons
                + " find=" + r.findOps + " union=" + r.unionOps
                + " pqPush=" + r.pqPushes + " pqPop=" + r.pqPops + " ns=" + r.timeNanos);
    }

    private static void topo(String name, Graph graph) {
        TopologicalSort sorter = new TopologicalSort();
        TopoResult r = sorter.run(graph);
        System.out.println("=== " + name + " ===");
        if (r.hasCycle) {
            System.out.println("Cycle detected, no topological order");
        } else {
            System.out.println("Order: " + r.order);
            System.out.println("Valid: " + sorter.isValidOrder(graph, r.order));
        }
        System.out.println("examined=" + r.edgesExamined + " push=" + r.queuePushes
                + " pop=" + r.queuePops + " ns=" + r.timeNanos);
        System.out.println();
    }
    private static void scc(String name, Graph graph) {
        TarjanSCC tarjan = new TarjanSCC();
        SCCResult r = tarjan.run(graph);
        System.out.println("=== " + name + " ===");
        System.out.println("Components: " + r.components.size());
        System.out.println(r.components);
        System.out.println("Valid partition: " + tarjan.isValidPartition(graph, r));
        System.out.println("Every component has one vertex: "
                + (r.components.size() == graph.getVertexCount()));
        System.out.println("examined=" + r.edgesExamined + " dfs=" + r.dfsCalls
                + " ns=" + r.timeNanos);
        System.out.println();
    }

}