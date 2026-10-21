package connectivity;

import java.util.List;

public class TopoResult {
    public final List<Integer> order;
    public final boolean hasCycle;
    public final long edgesExamined;
    public final long queuePushes;
    public final long queuePops;

    public TopoResult(List<Integer> order, boolean hasCycle,
                      long edgesExamined, long queuePushes, long queuePops) {
        this.order = order;
        this.hasCycle = hasCycle;
        this.edgesExamined = edgesExamined;
        this.queuePushes = queuePushes;
        this.queuePops = queuePops;
    }
}