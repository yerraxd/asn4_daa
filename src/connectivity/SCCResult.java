package connectivity;

import java.util.List;

public class SCCResult {
    public final List<List<Integer>> components;
    public final int[] componentOf;
    public final long edgesExamined;
    public final long dfsCalls;

    public SCCResult(List<List<Integer>> components, int[] componentOf,
                     long edgesExamined, long dfsCalls) {
        this.components = components;
        this.componentOf = componentOf;
        this.edgesExamined = edgesExamined;
        this.dfsCalls = dfsCalls;
    }
}