package dev.wido.deeplay.searchers;

import dev.wido.deeplay.graph.Graph;
import dev.wido.deeplay.graph.Position;
import dev.wido.deeplay.vertices.Vertex;
import dev.wido.deeplay.vertices.VertexPriorityPair;

import java.util.function.BiFunction;

public final class AStarSearcher extends DijkstraSearcher {

    private BiFunction<Position, Position, Integer> heuristic =
        (start, target) -> Math.abs(target.x() - start.x()) + Math.abs(target.y() - start.y());

    public AStarSearcher(Graph<? extends Vertex> graph) {
        super(graph);
    }

    public synchronized void replaceHeuristic(BiFunction<Position, Position, Integer> f) {
        this.heuristic = f;
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public synchronized long search(int startX, int startY, int targetX, int targetY) {
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
        return cost;
    }
}
