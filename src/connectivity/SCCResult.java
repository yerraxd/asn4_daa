package connectivity;

import java.util.List;

public class SCCResult {
    public final List<List<Integer>> components;

    public SCCResult(List<List<Integer>> components) {
        this.components = components;
    }
}