package dev.wido.deeplay.searchers;

import dev.wido.deeplay.Input;
import dev.wido.deeplay.graph.Graph;
import dev.wido.deeplay.vertices.Vertex;
import dev.wido.deeplay.vertices.VertexPriorityPair;

import java.util.HashMap;
import java.util.PriorityQueue;

public class DijkstraSearcher implements Searcher {
    private final PriorityQueue<VertexPriorityPair> frontier = new PriorityQueue<>();
    // TODO: Either make traceable path or remove
    private final HashMap<Vertex, Vertex> cameFrom  = new HashMap<>();
    private final HashMap<Vertex, Integer> pathCost = new HashMap<>();
    private final Graph<? extends Vertex> graph;

    public DijkstraSearcher(Graph<? extends Vertex> graph) {
        this.graph = graph;
    }

    @Override
    public int search(int startX, int startY, int targetX, int targetY) {
        var optionalStart = graph.get(startX, startY); // graph.get(Input.getStartCoords()) ?
        var optionalTarget = graph.get(targetX, targetY);
        if (optionalStart.isEmpty() || optionalTarget.isEmpty())
            throw new IllegalArgumentException("Either starting point or target doesn't exists");

        var start = optionalStart.get();
        var target = optionalTarget.get();
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

        return pathCost.get(target);
    }
}
