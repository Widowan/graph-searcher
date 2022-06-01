package dev.wido.deeplay.graph;

import dev.wido.deeplay.vertices.IntVertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class IntGraphTest {
    List<Optional<IntVertex>> map;
    IntGraph<IntVertex> graph;
    Comparator<IntVertex> verticesComparator;

    @BeforeEach
    void setUp() {
        map = Stream.of(null,
                1, 1, null,
                4, 3, 1,
                4, 5, 10)
            .skip(1)
            .map(v ->
                v != null
                ? Optional.of(new IntVertex(v))
                : Optional.<IntVertex>empty())
            .toList();

        graph = new IntGraph<>(map, 3,3);
        verticesComparator = Comparator.comparingInt(a -> a.count);
    }

    @Test
    void getEdges() {
        assertAll(
            () -> assertEquals(
                graph.getEdges(graph.get(2, 1).get())
                    .stream().sorted(verticesComparator).toList(),
                Stream.of(
                    graph.get(1, 1).get(),
                    graph.get(2, 2).get()
                ).sorted(verticesComparator).toList()
            ),

            () -> assertThrows(
                IllegalArgumentException.class,
                () -> graph.getEdges(new IntVertex(0))
            )
        );
    }

    @Test
    void find() {
        assertAll(
            () -> assertEquals(
                graph.find(graph.get(1, 1).get()).get(),
                new Position(1, 1)
            ),
            () -> assertThrows(IllegalArgumentException.class, () -> graph.get(3,2))
        );
    }

    @Test
    void get() {
        assertAll(
            () -> assertEquals(graph.get(2, 0), Optional.empty()),
            () -> assertEquals(graph.get(1,1), map.get(4)),
            () -> assertThrows(IllegalArgumentException.class, () -> graph.get(0, 3)),
            () -> assertThrows(IllegalArgumentException.class, () -> graph.get(3, 0)),
            () -> assertThrows(IllegalArgumentException.class, () -> graph.get(0, -1)),
            () -> assertThrows(IllegalArgumentException.class, () -> graph.get(-1, 0))
        );
    }

    @Test
    void costBetween() {
        assertAll(
            () -> assertEquals(
                graph.costBetween(map.get(0).get(), map.get(3).get()),
                OptionalInt.of(4),
                "Normal neighbours"
            ),

            () -> assertEquals(
                graph.costBetween(map.get(1).get(), map.get(0).get()),
                graph.costBetween(map.get(0).get(), map.get(1).get()),
                "Reverse path should equal normal one"
            ),

            () -> assertEquals(
                graph.costBetween(map.get(0).get(), map.get(4).get()),
                OptionalInt.empty(),
                "Unreachable path"
            ),

            () -> assertEquals(
                graph.costBetween(map.get(1).get(), new IntVertex(0)),
                OptionalInt.empty(),
                "Existing and not existing vertices"
            ),

            () -> assertEquals(
                graph.costBetween(new IntVertex(0), map.get(1).get()),
                OptionalInt.empty(),
                "Not-existing and existing vertices"
            )
        );
    }
}