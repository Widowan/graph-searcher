package dev.wido.deeplay;

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

        var costMap = new HashMap<VerticesPair, Integer>();
        var rows = 4;
        var cols = 4;

        for (int i = 0; i < map.size(); i++) {
            var row = i / rows;
            var col = i % cols;
            final var fi = i;
            Stream.of(
                    col > 0        ? map.get(i - 1)    : Optional.<IntVertex>empty(), // Left
                    col + 1 < cols ? map.get(i + 1)    : Optional.<IntVertex>empty(), // Right
                    row > 0        ? map.get(i - cols) : Optional.<IntVertex>empty(), // Above
                    row + 1 < rows ? map.get(i + cols) : Optional.<IntVertex>empty()) // Below
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(v -> costMap.putIfAbsent(new VerticesPair(map.get(fi).get(), v), v.value));
        }

        var graph = new WeightedGraph<>(map, 4, 4, costMap);
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