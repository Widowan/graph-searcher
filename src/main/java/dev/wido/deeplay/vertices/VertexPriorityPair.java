package dev.wido.deeplay.vertices;

// Lack of anonymous tuples strikes again!
public record VertexPriorityPair(
    Vertex vertex,
    int priority
) implements Comparable<VertexPriorityPair> {
    @Override
    public int compareTo(VertexPriorityPair other) {
        return Integer.compare(priority, other.priority());
    }
}
