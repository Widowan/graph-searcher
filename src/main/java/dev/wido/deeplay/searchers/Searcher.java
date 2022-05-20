package dev.wido.deeplay.searchers;

import dev.wido.deeplay.vertices.Vertex;

import java.util.List;
import java.util.OptionalInt;
import java.util.OptionalLong;

public interface Searcher {
    OptionalLong search(int startX, int startY, int targetX, int targetY);
    List<Vertex> tracePath();
}
