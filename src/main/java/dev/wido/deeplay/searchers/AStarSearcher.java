package dev.wido.deeplay.searchers;

import dev.wido.deeplay.graph.Graph;
import dev.wido.deeplay.graph.Position;
import dev.wido.deeplay.graph.WeightedGraph;
import dev.wido.deeplay.vertices.Vertex;
import dev.wido.deeplay.vertices.VertexPriorityPair;

import java.util.OptionalLong;
import java.util.function.BiFunction;

/**
 * @author Widowan
 * Searcher that implements A* (A-star) searching algorithm.
 * Used with some kind of {@link Graph} implementation.
 */
public final class AStarSearcher extends DijkstraSearcher {

    private BiFunction<Position, Position, Integer> heuristic =
        (start, target) -> Math.abs(target.x() - start.x()) + Math.abs(target.y() - start.y());

    /**
     * Instantiate new searcher object. Doesn't do any
     * computations under the hood, so this is a cheap operation.
     * @param graph graph to search on
     */
    public AStarSearcher(WeightedGraph<? extends Vertex> graph) {
        super(graph);
    }

    /**
     * Change heuristic function, which is one of the main properties of A* searcher
     * and can affect algorithm's speed drastically. Default one is Manhattan distance.
     * @param f new heuristic function that accepts starting and ending {@link Position positions}.
     */
    public synchronized void replaceHeuristic(BiFunction<Position, Position, Integer> f) {
        this.heuristic = f;
    }

    /**
     * Main method, do the searching. Expensive operation.
     * @param startX x coordinate of vertex to start searching from
     * @param startY y coordinate of vertex to start searching from
     * @param targetX x coordinate of vertex to search
     * @param targetY y coordinate of vertex to search
     * @return Optional value, containing None in case of target
     *        not being found, or some value if found. Can be negative
     * @throws IllegalArgumentException if either start or target vertex doesn't exist
     */
    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public synchronized OptionalLong search(int startX, int startY, int targetX, int targetY) {
        var optionalStart = graph.get(startX, startY);
        var optionalTarget = graph.get(targetX, targetY);
        if (optionalStart.isEmpty() || optionalTarget.isEmpty())
            throw new IllegalArgumentException("Either starting point or target doesn't exists");

        start  = optionalStart.get();
        target = optionalTarget.get();
        var targetPos = new Position(targetX, targetY);

        frontier.add(new VertexPriorityPair(start, 0));
        cameFrom.put(start, null);
        pathCost.put(start, 0);

        while (!frontier.isEmpty()) {
            var current = frontier.poll().vertex();

            if (current == target) break;

            for (var next : graph.getEdges(current)) {
                var newCost = pathCost.get(current) + graph.costBetween(current, next).getAsInt();
                if (!pathCost.containsKey(next) || newCost < pathCost.get(next)) {
                    pathCost.put(next, newCost);
                    var priority = newCost + heuristic.apply(graph.find(next).get(), targetPos);
                    frontier.add(new VertexPriorityPair(next, priority));
                    cameFrom.put(next, current);
                }
            }
        }

        var cost = pathCost.get(target);
        pathCost.clear();
        frontier.clear();
        return cost == null ? OptionalLong.empty() : OptionalLong.of(cost);
    }
}
