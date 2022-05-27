package dev.wido.deeplay;

import dev.wido.deeplay.graph.IntGraph;
import dev.wido.deeplay.searchers.AStarSearcher;

import java.io.IOException;
import java.util.List;

public class Solution {
    public static long getSolution(String field, String race) throws IOException {
        var input = Input.unpackParams(
            List.of(field, race, "4", "4", "0", "0", "3", "3"));
        var map = new Input().getField(input);

        var graph = new IntGraph<>(map, input.rows(), input.cols());
        var searcher = new AStarSearcher(graph);
        var result = searcher.search(input.startX(), input.startY(),
                                                  input.targetX(), input.targetY());

        return result.isPresent() ? result.getAsLong() : -1;
    }
}
