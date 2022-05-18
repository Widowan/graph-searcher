package dev.wido.deeplay.vertices;

import java.util.Objects;

// Lack of anonymous tuples strikes YET again!
public record VertexPair(
    Vertex v1,
    Vertex v2
) {
    public VertexPair(Vertex v1, Vertex v2) {
        this.v1 = v1.count < v2.count ? v1 : v2;
        this.v2 = v1.count > v2.count ? v1 : v2;
    }
}
