package dev.wido.deeplay.vertices;

import java.util.Objects;

public class Vertex {
    private static int totalCounter = 0;
    public final int count;

    public Vertex() {
        count = totalCounter;
        totalCounter += 1;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return count == vertex.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }
}
