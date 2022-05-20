package dev.wido.deeplay.searchers;

import dev.wido.deeplay.vertices.Vertex;

import java.util.List;

public interface Searcher {
    long search(int startX, int startY, int targetX, int targetY);
    List<Vertex> tracePath();
}
