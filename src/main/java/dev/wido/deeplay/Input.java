package dev.wido.deeplay;

import dev.wido.deeplay.vertices.IntVertex;
import dev.wido.deeplay.vertices.Vertex;

import java.util.*;

public class Input {
    private String race;
    private HashMap<String /* Enum? */, HashMap<Character, OptionalInt>> matchTable;

    public Input() {}

    public InputResult get(List<String> args) {
        var field = args.get(0);
        this.race = args.get(1);
        int rows = Integer.parseInt(args.get(2));
        int cols = Integer.parseInt(args.get(3));
        this.matchTable = new HashMap<>();
        matchTable.put("Human", new HashMap<>());
        matchTable.get("Human").put('S', OptionalInt.of(5));
        matchTable.get("Human").put('W', OptionalInt.of(2));
        matchTable.get("Human").put('T', OptionalInt.of(3));
        matchTable.get("Human").put('P', OptionalInt.of(1));
        matchTable.get("Human").put('#', OptionalInt.empty());

        matchTable.put("Swamper", new HashMap<>());
        matchTable.get("Swamper").put('S', OptionalInt.of(2));
        matchTable.get("Swamper").put('W', OptionalInt.of(2));
        matchTable.get("Swamper").put('T', OptionalInt.of(5));
        matchTable.get("Swamper").put('P', OptionalInt.of(2));
        matchTable.get("Swamper").put('#', OptionalInt.empty());

        matchTable.put("Woodman", new HashMap<>());
        matchTable.get("Woodman").put('S', OptionalInt.of(3));
        matchTable.get("Woodman").put('W', OptionalInt.of(3));
        matchTable.get("Woodman").put('T', OptionalInt.of(2));
        matchTable.get("Woodman").put('P', OptionalInt.of(2));
        matchTable.get("Woodman").put('#', OptionalInt.empty());

        return new InputResult(processField(field), rows, cols);
    }

    private OptionalInt matchElement(char ch) {
        return matchTable.get(race).get(ch);
    }

    private List<Optional<IntVertex>> processField(String field) {
        var list = new ArrayList<Optional<IntVertex>>();
        for (int i = 0; i < field.length(); i++) {
            var e = matchElement(field.charAt(i));
            list.add(e.isPresent() ? Optional.of(new IntVertex(e.getAsInt())) : Optional.empty());
        }
        return list;
    }

    public record InputResult(
        List<Optional<IntVertex>> map,
        int rows, int cols
    ) {}

    public static void printPath(List<? extends Vertex> tracedPath,
                                 List<? extends Optional<? extends Vertex>> map,
                                 int cols) {
        for (int i = 0; i < map.size(); i++) {
            String ch;
            var v = map.get(i);

            if (i % cols == 0) System.out.println();
            if (v.isEmpty()) ch = "/ ";
            else {
                if (tracedPath.contains(v.get())) ch = "@ ";
                else ch = ". ";
            }

            System.out.print(ch);
        }
    }
}
