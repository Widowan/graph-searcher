package dev.wido.deeplay.searchers;

import dev.wido.deeplay.graph.Graph;
import dev.wido.deeplay.graph.WeightedGraph;
import dev.wido.deeplay.vertices.Vertex;
import dev.wido.deeplay.vertices.VertexPriorityPair;

import java.util.*;

/**
 * @author Widowan
 * Searcher that implements Dijkstra's searching algorithm.
 * Used with some kind of {@link Graph} implementation.
 */
public class DijkstraSearcher implements Searcher {
    protected final PriorityQueue<VertexPriorityPair> frontier = new PriorityQueue<>();
    protected final HashMap<Vertex, Vertex> cameFrom  = new HashMap<>();
    protected final HashMap<Vertex, Integer> pathCost = new HashMap<>();
    protected final WeightedGraph<? extends Vertex> graph;
    protected Vertex target = null;
    protected Vertex start  = null;

    /**
     * Instantiate new searcher object. Doesn't do any
     * computations under the hood, so this is a cheap operation.
     * @param graph graph to search on
     */
    public DijkstraSearcher(WeightedGraph<? extends Vertex> graph) {
        this.graph = graph;
    }

    // TODO: Test negatives on graph
    /**
     * Main method, do the searching. Expensive operation. <p>
     * <em>Dijkstra doesn't support graphs with negative weights</em> and will throw.
     * @param startX x coordinate of vertex to start searching from
     * @param startY y coordinate of vertex to start searching from
     * @param targetX x coordinate of vertex to search
     * @param targetY y coordinate of vertex to search
     * @return Optional value, containing None in case of target
     *        not being found, or some value if found. Cannot be negative
     * @throws IllegalArgumentException if either start or target vertex doesn't exist
     * @throws IllegalStateException if cost during movement is found to be negative
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public synchronized OptionalLong search(int startX, int startY, int targetX, int targetY) {
        var optionalStart = graph.get(startX, startY);
        var optionalTarget = graph.get(targetX, targetY);
        if (optionalStart.isEmpty() || optionalTarget.isEmpty())
            throw new IllegalArgumentException("Either starting point or target doesn't exists");

        start  = optionalStart.get();
        target = optionalTarget.get();

        frontier.add(new VertexPriorityPair(start, 0));
        cameFrom.put(start, null);
        pathCost.put(start, 0);

        while (!frontier.isEmpty()) {
            var current = frontier.poll().vertex();

            if (current.equals(target)) break;

            for (var next : graph.getEdges(current)) {
                var costBetween = graph.costBetween(current, next).getAsInt();
                if (costBetween < 0)
                    throw new IllegalStateException("Negative cost");
                var newCost = pathCost.get(current) + graph.costBetween(current, next).getAsInt();
                if (!pathCost.containsKey(next) || newCost < pathCost.get(next)) {
                    pathCost.put(next, newCost);
                    frontier.add(new VertexPriorityPair(next, newCost));
                    cameFrom.put(next, current);
                }
            }
        }

        var cost = pathCost.get(target);
        pathCost.clear();
        frontier.clear();
        return cost == null ? OptionalLong.empty() : OptionalLong.of(cost);
    }

    // Tracing path backwards
    /**
     * @return a list containing all the visited vertices ordered from first to last.
     *        Uses {@link Collections#reverse(List)} under the hood, making it 2*O(n).
     */
    @Override
    public synchronized List<Vertex> tracePath() {
        if (start == null || target == null) return List.of();
        var path = new ArrayList<Vertex>();

        var current = target;
        while (current != start) {
            path.add(current);
            current = cameFrom.get(current);
            if (current == null) return List.of();
        }

        path.add(start);
        Collections.reverse(path); // O(n)
        return path;
    }
}
