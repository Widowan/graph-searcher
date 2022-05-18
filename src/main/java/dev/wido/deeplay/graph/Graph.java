package dev.wido.deeplay.graph;

import dev.wido.deeplay.vertices.Vertex;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface Graph<T extends Vertex> {
    List<T> getEdges(Vertex v);

    OptionalInt costBetween(Vertex v1, Vertex v2);

    Optional<T> get(int x, int y);
}
