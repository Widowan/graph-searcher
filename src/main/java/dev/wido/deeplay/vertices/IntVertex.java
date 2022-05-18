package dev.wido.deeplay.vertices;

final public class IntVertex extends Vertex {
    public final int value;

    public IntVertex(int value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return "IntVertex[" + value + "@" + count + "]";
    }
}
