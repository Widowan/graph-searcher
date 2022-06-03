package dev.wido.deeplay.searchers;

import dev.wido.deeplay.graph.Graph;
import dev.wido.deeplay.graph.IntGraph;
import dev.wido.deeplay.graph.WeightedGraph;
import dev.wido.deeplay.vertices.IntVertex;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GenericSearcherTest {
    private Searcher clearSearcher;
    private Searcher wallSearcher;
    private Searcher deadSearcher;

    public static final Function<Stream<Integer>, List<Optional<IntVertex>>> createMap =
        s -> s
            .skip(1)
            .map(v -> v != null
                ? Optional.of(new IntVertex(v))
                : Optional.<IntVertex>empty())
            .toList();

    public static final List<Optional<IntVertex>> clearMap = createMap.apply(
        Stream.of(null,
            0, 2, 2, 9,
            1, 9, 1, 9,
            9, 9, 1, 9,
            9, 9, 3, 0
        )
    ); // 0, 1, 2, 6, 10, 14, 15

    public static final List<Optional<IntVertex>> wallMap = createMap.apply(
        Stream.of(null,
            1   , 1   , null, 99  ,
            null, 1   , 1   , null,
            99  , null, 1   , 1   ,
            99  , 99  , null, 1
        )
    ); // 0, 1, 5, 6, 10, 11, 15

    public static final List<Optional<IntVertex>> deadMap = createMap.apply(
        Stream.of(null,
            1   , 1   , 1   , 1,
            1   , 1   , 1   , 1,
            1   , 1   , 1   , null,
            1   , 1   , null, 1
        )
    ); // []

    public void setUp(Class<? extends Searcher> searcherClass)
            throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException
    {
        var clearGraph = new IntGraph<>(clearMap, 4, 4);
        var wallGraph = new IntGraph<>(wallMap, 4, 4);
        var deadGraph = new IntGraph<>(deadMap, 4, 4);

        // Initialize passed class, be it AStar, Dijkstra, ...
        clearSearcher = searcherClass.getDeclaredConstructor(WeightedGraph.class).newInstance(clearGraph);
        wallSearcher = searcherClass.getDeclaredConstructor(WeightedGraph.class).newInstance(wallGraph);
        deadSearcher = searcherClass.getDeclaredConstructor(WeightedGraph.class).newInstance(deadGraph);
    }

    void searchClear() {
        assertAll(
            () -> assertEquals(
                OptionalLong.of(9),
                clearSearcher.search(0, 0, 3, 3)
            ),
            () -> assertEquals(
                OptionalLong.of(6),
                clearSearcher.search(0, 0, 2, 2)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> clearSearcher.search(0, 0, -1, -1)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> clearSearcher.search(-1, -1, 0, 0)
            )
        );
    }

    void searchWall() {
        assertAll(
            () -> assertEquals(
                OptionalLong.of(6),
                wallSearcher.search(0, 0, 3, 3)
            ),
            () -> assertEquals(
                OptionalLong.of(4),
                wallSearcher.search(0, 0, 2, 2)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> wallSearcher.search(0, 0, 0, 1)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> wallSearcher.search(-1, -1, 0, 0)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> clearSearcher.search(0, 0, -1, -1)
            )
        );
    }

    void searchDead() {
        assertAll(
            () -> assertEquals(
                OptionalLong.empty(),
                deadSearcher.search(0, 0, 3, 3)
            ),
            () -> assertThrows(
                IllegalArgumentException.class,
                () -> deadSearcher.search(0, 0, 3, 2)
            )
        );
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void tracePath() {
        clearSearcher.search(0, 0, 3, 3);
        var clearPath = Stream.of(0, 1, 2, 6, 10, 14, 15).map(i -> clearMap.get(i).get()).toList();

        wallSearcher.search(0, 0, 3, 3);
        var wallPath = Stream.of(0, 1, 5, 6, 10, 11, 15).map(i -> wallMap.get(i).get()).toList();

        deadSearcher.search(0, 0, 3, 3);

        assertAll(
            () -> assertEquals(
                clearPath,
                clearSearcher.tracePath()
            ),
            () -> assertEquals(
                wallPath,
                wallSearcher.tracePath()
            ),
            () -> assertEquals(
                List.of(),
                deadSearcher.tracePath()
            )
        );
    }
}