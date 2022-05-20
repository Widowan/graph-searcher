package dev.wido.deeplay;

import dev.wido.deeplay.vertices.IntVertex;
import dev.wido.deeplay.vertices.Vertex;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Input {
    private String race;
    private final HashMap<String, HashMap<Character, OptionalInt>> matchTable = new HashMap<>();

    public Input() {}

    public InputResult get(List<String> args) throws IOException, URISyntaxException {
        var field = args.get(0);
        this.race   = args.get(1);
        int cols    = Integer.parseInt(args.get(2));
        int rows    = Integer.parseInt(args.get(3));
        int startX  = Integer.parseInt(args.get(4));
        int startY  = Integer.parseInt(args.get(5));
        int targetX = Integer.parseInt(args.get(6));
        int targetY = Integer.parseInt(args.get(7));

        var propPath = getPropPath();
        var propFile = Files.newInputStream(propPath);

        var props = new Properties();
        props.load(propFile);
        fillMatchTable(field, props);

        return new InputResult(processField(field), rows, cols, startX, startY, targetX, targetY);
    }

    private Path getPropPath() throws URISyntaxException {
        var propPath = Paths.get(
            new File(
                Input.class.getProtectionDomain().getCodeSource().getLocation().toURI()
            ).getParent() + "/config.properties");

        if (!Files.exists(propPath))
            propPath = Paths.get("src/main/resources/config.properties");

        return propPath;
    }

    private void fillMatchTable(String field, Properties props) {
        matchTable.putIfAbsent(race, new HashMap<>());

        for (int i = 0; i < field.length(); i++) {
            var ch = field.charAt(i);
            if (matchTable.get(race).containsKey(ch))
                continue;

            var stringProp = props.getProperty(race + "." + ch);
            var prop = stringProp == null || stringProp.equals("")
                ? OptionalInt.empty()
                : OptionalInt.of(Integer.parseInt(stringProp));
            matchTable.get(race).put(ch, prop);
        }
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
        int rows,    int cols,
        int startX,  int startY,
        int targetX, int targetY
    ) {}

    public static void printPath(List<? extends Vertex> tracedPath,
                                 List<? extends Optional<? extends Vertex>> map,
                                 int cols, int startX, int startY,
                                 int targetX, int targetY) {
        for (int i = 0; i < map.size(); i++) {
            String ch;
            var v = map.get(i);

            if (i % cols == 0) System.out.println();

            if (i % cols == startX && i / cols == startY)
                ch = "+ ";
            else if (i % cols == targetX && i / cols == targetY)
                ch = "- ";
            else if (v.isEmpty())
                ch = "/ ";
            else {
                if (tracedPath.contains(v.get()))
                    ch = "@ ";
                else
                    ch = ". ";
            }

            System.out.print(ch);
        }

        System.out.println();
    }
}
