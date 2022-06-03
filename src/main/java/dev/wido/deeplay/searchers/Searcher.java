package dev.wido.deeplay.searchers;

import dev.wido.deeplay.vertices.Vertex;

import java.util.List;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * An interface defining searcher that works with {@link dev.wido.deeplay.graph.Graph} implementations.
 */
public interface Searcher {
    /**
     * Actual searching method from any position to any position.
     * @param startX x coordinate of vertex to start searching from
     * @param startY y coordinate of vertex to start searching from
     * @param targetX x coordinate of vertex to search
     * @param targetY y coordinate of vertex to search
     * @return {@link OptionalLong} value that may or may not be negative
     *        in case target vertex is found, or empty if not found.
     * @throws IllegalArgumentException if coordinates are out of
     *        bounds or one of specified vertices doesn't exist
     */
    OptionalLong search(int startX, int startY, int targetX, int targetY);

    /**
     * Method that returns the search path - list of
     * vertices that were visited on correct path.
     * <p>
     * Ordered from first (start) to last (target).
     * @return List that represents path
     */
    List<Vertex> tracePath();
}
