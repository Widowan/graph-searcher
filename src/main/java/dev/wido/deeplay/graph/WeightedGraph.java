package dev.wido.deeplay.graph;

import dev.wido.deeplay.vertices.SortedVerticesPair;
import dev.wido.deeplay.vertices.Vertex;
import dev.wido.deeplay.vertices.VerticesPair;

import java.util.*;
import java.util.stream.Stream;

/**
 * Abstract skeleton of a weighted graph implementation.
 * <p>
 * Important to note that it uses {@link Map} of VerticesPairs and
 * Integers as {@code costMap} under the hood, so you need to consider its impact.
 * @param <T> Concrete type of vertex to create graph of
 */
abstract public class WeightedGraph<T extends Vertex> implements Graph<T> {
    private final List<? extends Optional<? extends T>> map;
    private final Map<VerticesPair, Integer> costMap;
    private final int rows;
    private final int cols;

    /**
     * Create graph. By itself computes nothing, assigns costMap internally.
     * @param map one-dimensional list containing all vertices.
     *           Basically just flattened two-dimensional vertices map
     * @param rows number of rows graph has
     * @param cols number of columns graph has
     * @param costMap A {@link Map} that specifies cost of moving between
     *              two vertices (graph's weights)
     */
    public WeightedGraph(List<? extends Optional<? extends T>> map, int rows, int cols,
                         Map<VerticesPair, Integer> costMap) {
        this.map = map;
        this.cols = cols;
        this.rows = rows;
        this.costMap = costMap;
    }

    /**
     * Get edges of specified graph. Follows interface's definition: {@link Graph#getEdges(Vertex)}
     * @param v vertex to get edges to
     * @throws IllegalArgumentException if specified vertex doesn't exist
     * @return {@link List} containing edges of specified vertex
     */
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

    /**
     * Get cost of moving between two vertices.
     * <p>
     * <em>Depending on graph implementation,
     * it may or may not be symmetrical</em>
     * (so moving from v1 to v2 is not necessarily the same as moving from v2 to v1).
     * @param v1 first vertex
     * @param v2 second vertex
     * @return movement cost, {@link OptionalInt} as it <em>can</em> be negative or not exists at all
     */
    public OptionalInt costBetween(Vertex v1, Vertex v2) {
        var c = costMap.get(new SortedVerticesPair(v1, v2));
        return c != null ? OptionalInt.of(c) : OptionalInt.empty();
    }

    /**
     * Get vertex by specifying its coordinates.
     * @param x x coordinate of a vertex
     * @param y y coordinate of a vertex
     * @return {@link Optional} value of vertex
     * @throws IllegalArgumentException if coordinates do not exist (less than zero or out of bounds)
     */
    @Override
    public Optional<T> get(int x, int y) {
        if (x >= cols || x < 0 || y < 0 || y >= rows)
            throw new IllegalArgumentException("Out of bounds access");
        return map.get(y * cols + x).map(it -> (T)it);
    }

    /**
     * Find specified vertex's position
     * @param v vertex to find position of
     * @return Optional value of vertex's {@link Position}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Optional<Position> find(Vertex v) {
        var idx = map.indexOf(Optional.of((T)v));
        if (idx == -1) return Optional.empty();

        return Optional.of(new Position(idx / cols, idx % cols));
    }
}
