package dev.wido.deeplay;

import dev.wido.deeplay.graph.IntGraph;
import dev.wido.deeplay.searchers.AStarSearcher;

import java.util.Arrays;
import java.util.List;

public class Main {
    // Don't forget Solution.getResult as per req
    public static void main(String[] args) {
        var input = new Input().get(Arrays.asList(args));

        var graph = new IntGraph<>(input.map(), input.rows(), input.cols());
        var searcher = new AStarSearcher(graph);
        var result = searcher.search(1, 2, input.cols()-1, input.rows()-1);
        System.out.println(result);

        var tracedPath = searcher.tracePath();
        Input.printPath(tracedPath, input.map(), input.cols());
    }
}