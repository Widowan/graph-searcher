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

public final class Input {
    private String race;
    private Properties props;
    private final HashMap<String, HashMap<Character, OptionalInt>> matchTable = new HashMap<>();

    public Input() {}

    public List<Optional<IntVertex>> getField(InputParams params) throws IOException {
        var field = params.field();
        race = params.race();

        if (props == null) {
            var propPath = getPropPath();
            var propFile = Files.newInputStream(propPath);
            props = new Properties();
            props.load(propFile);
        }

        fillMatchTable(field);

        return processField(field);
    }

    public static InputParams unpackParams(List<String> args) {
        if (args.size() != 8) throw new IllegalArgumentException("Illegal count of arguments");
        try {
            String field = args.get(0);
            String race  = args.get(1);
            int cols     = Integer.parseInt(args.get(2));
            int rows     = Integer.parseInt(args.get(3));
            int startX   = Integer.parseInt(args.get(4));
            int startY   = Integer.parseInt(args.get(5));
            int targetX  = Integer.parseInt(args.get(6));
            int targetY  = Integer.parseInt(args.get(7));
            return new InputParams(field, race, cols, rows, startX, startY, targetX, targetY);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error during argument parsing", e);
        }
    }

    private Path getPropPath() throws IOException {
        try {
            var propPath = Paths.get(
                new File(
                    Input.class.getProtectionDomain().getCodeSource().getLocation().toURI()
                ).getParent() + "/config.properties");

            if (!Files.exists(propPath))
                propPath = Paths.get("src/main/resources/config.properties");

            return propPath;
        } catch (URISyntaxException e) {
            throw new IOException(e.getMessage());
        }
    }

    private void fillMatchTable(String field) {
        if (props.keySet().stream().noneMatch(i -> i.toString().startsWith(race)))
            throw new IllegalArgumentException("Race doesn't exists");

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

    public record InputParams(
        String field,
        String race,
        int cols,    int rows,
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

            if (i % cols == 0 && i != 0) System.out.println();

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
