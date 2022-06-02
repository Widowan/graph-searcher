package dev.wido.deeplay.vertices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VerticesTest {
    Vertex v1;
    Vertex v2;
    IntVertex iv1;
    IntVertex iv2;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        var field = Vertex.class.getDeclaredField("totalCounter");
        field.setAccessible(true);
        field.setInt(null, 0);

        v1 = new Vertex();
        v2 = new Vertex();
        iv1 = new IntVertex(0);
        iv2 = new IntVertex(1);
    }

    @Test
    void testEqualsVertex() {
        assertAll(
            () -> assertEquals(v1, v1),
            () -> assertNotEquals(v1, v2),
            () -> assertNotEquals(v1, iv1)
        );
    }

    @Test
    void testCountVertex() {
        assertAll(
            () -> assertEquals(0, v1.count),
            () -> assertEquals(1, v2.count)
        );
    }

    @Test
    void testEqualsIntVertex() {
        assertAll(
            () -> assertEquals(iv1, iv1),
            () -> assertNotEquals(iv1, iv2),
            () -> assertNotEquals(iv1, v1)
        );
    }

    @Test
    void testCountIntVertex() {
        assertAll(
            () -> assertEquals(2, iv1.count),
            () -> assertEquals(3, iv2.count)
        );
    }

    @Test
    void testHashCodeVertex() {
        assertAll(
            () -> assertNotEquals(v1.hashCode(), v2.hashCode()),
            () -> assertNotEquals(v1.hashCode(), iv1.hashCode())
        );
    }
}