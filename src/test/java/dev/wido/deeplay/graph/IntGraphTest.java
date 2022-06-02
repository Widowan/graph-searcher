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
                Stream.of(
                    graph.get(1, 1).get(),
                    graph.get(2, 2).get()
                ).sorted(verticesComparator).toList(),

                graph.getEdges(graph.get(2, 1).get())
                    .stream().sorted(verticesComparator).toList()
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
                new Position(1, 1),
                graph.find(graph.get(1, 1).get()).get()
            ),
            () -> assertThrows(IllegalArgumentException.class, () -> graph.get(3,2))
        );
    }

    @Test
    void get() {
        assertAll(
            () -> assertEquals(Optional.empty(), graph.get(2, 0)),
            () -> assertEquals(map.get(4), graph.get(1,1)),
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
                OptionalInt.of(4),
                graph.costBetween(map.get(0).get(), map.get(3).get()),
                "Normal neighbours"
            ),

            () -> assertEquals(
                graph.costBetween(map.get(1).get(), map.get(0).get()),
                graph.costBetween(map.get(0).get(), map.get(1).get()),
                "Reverse path should equal normal one"
            ),

            () -> assertEquals(
                OptionalInt.empty(),
                graph.costBetween(map.get(0).get(), map.get(4).get()),
                "Unreachable path"
            ),

            () -> assertEquals(
                OptionalInt.empty(),
                graph.costBetween(map.get(1).get(), new IntVertex(0)),
                "Existing and not existing vertices"
            ),

            () -> assertEquals(
                OptionalInt.empty(),
                graph.costBetween(new IntVertex(0), map.get(1).get()),
                "Not-existing and existing vertices"
            )
        );
    }
}