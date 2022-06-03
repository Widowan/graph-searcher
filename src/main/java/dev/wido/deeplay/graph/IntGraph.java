package dev.wido.deeplay.graph;

import dev.wido.deeplay.vertices.IntVertex;
import dev.wido.deeplay.vertices.SortedVerticesPair;
import dev.wido.deeplay.vertices.VerticesPair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Type of weighted graph made specifically for the task. Used with IntVertex,
 * uses symmetrical cost map generation.
 * @param <T> Type of vertex to use, IntVertex or more concrete
 */
final public class IntGraph<T extends IntVertex> extends WeightedGraph<T> {

    /**
     * Create graph. Generates costMap instantly, so it is a costly operation.
     * @param map vertex map as per
     *       {@link WeightedGraph#WeightedGraph(List, int, int, Map)} WeightedGraph::new}
     *       description
     * @param rows number of rows graph has
     * @param cols number of columns graph has
     */
    public IntGraph(List<? extends Optional<? extends T>> map,
                    int rows, int cols) {
        super(map, rows, cols, createCostMap(map, rows, cols));
    }

    private static <T extends IntVertex> HashMap<VerticesPair, Integer> createCostMap(
        List<? extends Optional<? extends T>> map, int rows, int cols)
    {
        var costMap = new HashMap<VerticesPair, Integer>();
        for (int i = 0; i < map.size(); i++) {
            var row = i / cols;
            var col = i % cols;
            var cur = map.get(i);
            if (cur.isPresent())
                Stream.of(
                        col > 0        ? map.get(i - 1)    : Optional.<IntVertex>empty(), // Left
                        col + 1 < cols ? map.get(i + 1)    : Optional.<IntVertex>empty(), // Right
                        row > 0        ? map.get(i - cols) : Optional.<IntVertex>empty(), // Above
                        row + 1 < rows ? map.get(i + cols) : Optional.<IntVertex>empty()) // Below
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(v -> costMap.putIfAbsent(new SortedVerticesPair(cur.get(), v), v.value));
        }

        return costMap;
    }
}
