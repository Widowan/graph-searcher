package dev.wido.deeplay.vertices;

/**
 * A type of vertex that hold an int value inside.
 */
public class IntVertex extends Vertex {
    /**
     * The value vertex holds
     */
    public final int value;

    /**
     * Create new vertex with specified value.
     * @param value value vertex to hold
     */
    public IntVertex(int value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return "IntVertex[" + value + "@" + count + "]";
    }
}
