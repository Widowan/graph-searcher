package dev.wido.deeplay.searchers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

public class AStarSearcherTest extends GenericSearcherTest {

    @BeforeEach
    void setUp() throws InvocationTargetException, NoSuchMethodException,
        InstantiationException, IllegalAccessException
    {
        super.setUp(AStarSearcher.class);
    }

    @Override
    @Test
    void searchClear() {
        super.searchClear();
    }

    @Override
    @Test
    void searchDead() {
        super.searchDead();
    }

    @Override
    @Test
    void searchWall() {
        super.searchWall();
    }

    @Override
    @Test
    void tracePath() {
        super.tracePath();
    }
}
