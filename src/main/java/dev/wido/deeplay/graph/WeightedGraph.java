package dev.wido.deeplay.graph;

import dev.wido.deeplay.vertices.Vertex;
import dev.wido.deeplay.vertices.VerticesPair;

import java.util.*;
import java.util.stream.Stream;

public class WeightedGraph<T extends Vertex> implements Graph<T> {
    private final List<? extends Optional<? extends T>> map;
    private final Map<VerticesPair, Integer> costMap;
    private final int rows;
    private final int cols;

    public WeightedGraph(List<? extends Optional<? extends T>> map, int rows, int cols,
                         Map<VerticesPair, Integer> costMap) {
        this.map = map;
        this.cols = cols;
        this.rows = rows;
        this.costMap = costMap;
    }

    // TODO: Test it
    public WeightedGraph(List<? extends List<? extends Optional<? extends T>>> map,
                         Map<VerticesPair, Integer> costMap) {
        this.rows = map.size();
        this.cols = map.get(0).size();
        if (map.stream().anyMatch(it -> it.size() != this.cols))
            throw new IllegalArgumentException("List is not uniformly sized");
        this.map = map.stream().flatMap(Collection::stream).toList();
        this.costMap = costMap;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getEdges(Vertex v) {
        var idx = map.indexOf(Optional.of((T)v));
        if (idx == -1) throw new IllegalArgumentException("Vertex doesn't exists");
        var row = idx / cols;
        var col = idx % cols;
        return Stream.of(
            col > 0        ? map.get(idx - 1)    : Optional.<T>empty(), // Left
            col + 1 < cols ? map.get(idx + 1)    : Optional.<T>empty(), // Right
            row > 0        ? map.get(idx - cols) : Optional.<T>empty(), // Above
            row + 1 < rows ? map.get(idx + cols) : Optional.<T>empty()  // Below
        ).filter(Optional::isPresent).map(Optional::get).map(it -> (T)it).toList();
    }

    @Override
    public OptionalInt costBetween(Vertex v1, Vertex v2) {
        var c = costMap.get(new VerticesPair(v1, v2));
        return c != null ? OptionalInt.of(c) : OptionalInt.empty();
    }

    @Override
    public Optional<T> get(int x, int y) {
        if (x >= cols || x < 0 || y < 0 || y >= rows)
            throw new IllegalArgumentException("Out of bounds access");
        return map.get(y * cols + x).map(it -> (T)it);
    }

    @SuppressWarnings("unchecked")
    public Optional<Position> find(Vertex v) {
        var idx = map.indexOf(Optional.of((T)v));
        if (idx == -1) return Optional.empty();

        return Optional.of(new Position(idx / rows, idx % cols));
    }
}
