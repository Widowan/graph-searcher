package dev.wido.deeplay;

import dev.wido.deeplay.graph.IntGraph;
import dev.wido.deeplay.searchers.AStarSearcher;

import java.util.Arrays;
import java.util.List;

public class Main {
    // Don't forget Solution.getResult as per req
    public static void main(String[] args) throws Exception {
        var input = new Input().get(Arrays.asList(args));

        var graph = new IntGraph<>(input.map(), input.rows(), input.cols());
        var searcher = new AStarSearcher(graph);
        var result = searcher.search(input.startX(), input.startY(), input.targetX(), input.targetY());
        System.out.println(result.isEmpty() ? "No route found" : result.getAsLong());

        var tracedPath = searcher.tracePath();
        Input.printPath(tracedPath, input.map(), input.cols(),
            input.startX(), input.startY(), input.targetX(), input.targetY());
    }
}