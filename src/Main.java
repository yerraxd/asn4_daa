import graph.Edge;
import graph.Graph;
import mst.Kruskal;
import mst.MSTResult;

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
        print(new Kruskal().run(g));

        Graph d = new Graph(5, false);
        d.addEdge(0, 1, 1);
        d.addEdge(1, 2, 2);
        d.addEdge(3, 4, 5);
        print(new Kruskal().run(d));
    }

    private static void print(MSTResult r) {
        for (Edge e : r.edges) {
            System.out.println(e);
        }
        System.out.println("weight=" + r.totalWeight + " connected=" + r.connected);
        System.out.println("examined=" + r.edgesExamined + " cmp=" + r.comparisons
                + " find=" + r.findOps + " union=" + r.unionOps + " ns=" + r.timeNanos);
    }
}