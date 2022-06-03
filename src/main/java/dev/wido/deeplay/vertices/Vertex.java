package dev.wido.deeplay.vertices;

import java.util.Objects;

/**
 * Simple class describing generic vertex that doesn't hold any value.
 * <p>
 * Even without having any traits, all vertices are unique by having internal static counter.
 * The counter has {@code int} value, so the class is limited to 2^32 (4 billion) unique vertices.
 */
public class Vertex {
    private static int totalCounter = 0;
    /**
     * Get vertex's count (its unique id)
     */
    public final int count;

    /**
     * Create new vertex
     */
    public Vertex() {
        count = totalCounter;
        totalCounter += 1;
    }

    /**
     * Compare two vertices by comparing their counts
     * @param o other vertex
     * @return equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return count == vertex.count;
    }

    /**
     * Vertex's hashcode is the hash of its count
     * @return hashed count
     */
    @Override
    public int hashCode() {
        return Objects.hash(count);
    }
}
