package dev.wido.deeplay.graph;

import dev.wido.deeplay.vertices.Vertex;

import java.util.List;
import java.util.Optional;

/**
 * Generic interface for describing 2 dimensional graphs that can provide edges of vertex,
 * cost between two vertices, get vertex by coordinates and find vertex.
 * @param <T> Type of specific {@link Vertex}
 */
public interface Graph<T extends Vertex> {
    /**
     * Get vertices adjacent to specified. It will not return walls (since they are
     * not vertices) nor vertices from next line that should be the edge of a graph.
     * <p>
     * For example, considering following graph:
     * <pre>
     *     0 1
     *     2 3 </pre>
     * getEdges on vertex {@code 1} will return vertices 0 and 3.
     * @param v vertex to get edges to
     * @return List containing edges, no order is guaranteed
     */
    List<T> getEdges(Vertex v);

    /**
     * Get vertex by specifying its coordinates.
     * @param x x coordinate of a vertex
     * @param y y coordinate of a vertex
     * @return {@link Optional} value of vertex as it may not exist at all
     */
    Optional<T> get(int x, int y);

    /**
     * Find specified vertex's {@link Position}
     * @param v vertex to find position of
     * @return {@link Optional} value of {@link Position} containing coordinates
     */
    Optional<Position> find(Vertex v);
}
