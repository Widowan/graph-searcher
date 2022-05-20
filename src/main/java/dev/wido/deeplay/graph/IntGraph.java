package dev.wido.deeplay.graph;

import dev.wido.deeplay.vertices.IntVertex;
import dev.wido.deeplay.vertices.VerticesPair;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

final public class IntGraph<T extends IntVertex> extends WeightedGraph<T> {
    public IntGraph(List<? extends Optional<? extends T>> map,
                    int rows, int cols) {
        super(map, rows, cols, createCostMap(map, rows, cols));
    }

    private static <T extends IntVertex> HashMap<VerticesPair, Integer> createCostMap(
        List<? extends Optional<? extends T>> map, int rows, int cols)
    {
        var costMap = new HashMap<VerticesPair, Integer>();
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

        return costMap;
    }
}
