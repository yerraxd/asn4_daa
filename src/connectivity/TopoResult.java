package connectivity;

import java.util.List;

public class TopoResult {
    public final List<Integer> order;
    public final boolean hasCycle;
    public final long edgesExamined;
    public final long queuePushes;
    public final long queuePops;
    public final long timeNanos;

    public TopoResult(List<Integer> order, boolean hasCycle,
                      long edgesExamined, long queuePushes, long queuePops, long timeNanos) {
        this.order = order;
        this.hasCycle = hasCycle;
        this.edgesExamined = edgesExamined;
        this.queuePushes = queuePushes;
        this.queuePops = queuePops;
        this.timeNanos = timeNanos;
    }
}