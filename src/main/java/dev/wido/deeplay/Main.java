package dev.wido.deeplay;

import dev.wido.deeplay.graph.IntGraph;
import dev.wido.deeplay.graph.WeightedGraph;
import dev.wido.deeplay.searchers.AStarSearcher;
import dev.wido.deeplay.searchers.DijkstraSearcher;
import dev.wido.deeplay.vertices.IntVertex;
import dev.wido.deeplay.vertices.VerticesPair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Main {
    // Don't forget Solution.getResult as per req
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static void main(String[] args) {
        var map = List.of(
            Optional.of(new IntVertex(5)), Optional.of(new IntVertex(3)), Optional.of(new IntVertex(2)), Optional.of(new IntVertex(5)),
            Optional.of(new IntVertex(2)), Optional.of(new IntVertex(3)), Optional.of(new IntVertex(1)), Optional.of(new IntVertex(1)),
            Optional.of(new IntVertex(3)), Optional.of(new IntVertex(1)), Optional.of(new IntVertex(3)), Optional.of(new IntVertex(3)),
            Optional.of(new IntVertex(1)), Optional.of(new IntVertex(2)), Optional.of(new IntVertex(1)), Optional.of(new IntVertex(1))
        );

        var rows = 4;
        var cols = 4;

        var graph = new IntGraph<>(map, rows, cols);
        var searcher = new AStarSearcher(graph);
        var result = searcher.search(0, 0, 3, 3);
        System.out.println(result);

        var tracedPath = searcher.tracePath();
        for (int i = 0; i < map.size(); i++) {
            if (i % rows == 0) System.out.println();
            System.out.printf(tracedPath.contains(map.get(i).get()) ? "X " : "_ ");
        }
    }
}