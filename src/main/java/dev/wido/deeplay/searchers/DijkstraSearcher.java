package dev.wido.deeplay.searchers;

import dev.wido.deeplay.graph.Graph;
import dev.wido.deeplay.vertices.Vertex;
import dev.wido.deeplay.vertices.VertexPriorityPair;

import java.util.*;

public class DijkstraSearcher implements Searcher {
    protected final PriorityQueue<VertexPriorityPair> frontier = new PriorityQueue<>();
    protected final HashMap<Vertex, Vertex> cameFrom  = new HashMap<>();
    protected final HashMap<Vertex, Integer> pathCost = new HashMap<>();
    protected final Graph<? extends Vertex> graph;
    protected Vertex target = null;
    protected Vertex start  = null;

    public DijkstraSearcher(Graph<? extends Vertex> graph) {
        this.graph = graph;
    }

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
                // noinspection OptionalGetWithoutIsPresent; since we're getting it from edges
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
