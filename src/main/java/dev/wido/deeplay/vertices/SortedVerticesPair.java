package dev.wido.deeplay.vertices;

// Lack of anonymous tuples strikes YET again!
public record SortedVerticesPair(Vertex v1, Vertex v2) implements VerticesPair {
    public SortedVerticesPair(Vertex v1, Vertex v2) {
        this.v1 = v1.count < v2.count ? v1 : v2;
        this.v2 = v1.count > v2.count ? v1 : v2;
    }
}
