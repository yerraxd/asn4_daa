package connectivity;

import java.util.List;

public class SCCResult {
    public final List<List<Integer>> components;
    public final int[] componentOf;

    public SCCResult(List<List<Integer>> components, int[] componentOf) {
        this.components = components;
        this.componentOf = componentOf;
    }
}