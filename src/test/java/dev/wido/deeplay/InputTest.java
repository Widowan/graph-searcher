package dev.wido.deeplay;

import dev.wido.deeplay.vertices.IntVertex;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class InputTest {
    private final Input input = new Input();
    private final Input.InputParams correctParams = new Input.InputParams(
        "STWSWTPPTPTTPWPP", "Human", 4, 4, 0, 0, 3, 3
    );

    @Test
    @Order(1)
    void unpackParams() {
        var args = new ArrayList<>(List.of("STWSWTPPTPTTPWPP", "Human", "4", "4", "0", "0", "3", "3"));

        assertAll(
            () -> assertEquals(Input.unpackParams(args), correctParams),
            () -> assertThrows(IllegalArgumentException.class, () -> Input.unpackParams(List.of())),
            () -> {
                args.set(2, "Unparseable string");
                assertThrows(IllegalArgumentException.class, () -> Input.unpackParams(args));
            }
        );
    }

    @Test
    @Order(2)
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    void getField() {
        var correctMap = Stream.of(
            null, // Avoid IDEA align
            5, 3, 2, 5,
            2, 3, 1, 1,
            3, 1, 3, 3,
            1, 2, 1, 1
        ).skip(1)
        .map(v -> new IntVertex(v) {
            public boolean equals(Object o) {
                return ((IntVertex)o).value == this.value;
            }
        }).map(Optional::of).collect(Collectors.toList());

        var incorrectParamsRace = Input.unpackParams(List.of(
            "STWSWTPPTPTTPWPP", "\0", "4", "4", "0", "0", "3", "3"
        ));

        var incorrectParamsFieldNotExistingElement = Input.unpackParams(List.of(
            "\0TWSWTPPTPTTPWPP", "Human", "4", "4", "0", "0", "3", "3"
        ));

        assertAll(
            () -> assertDoesNotThrow(
                // Order is important due to overriding above
                () -> assertEquals(correctMap, input.getField(correctParams))
            ),
            () -> assertAll("Incorrect parameters",
                () -> assertThrows(IllegalArgumentException.class,
                    () -> input.getField(incorrectParamsRace),
                    "Incorrect race specified"
                ),
                () -> assertDoesNotThrow(
                    () -> input.getField(incorrectParamsFieldNotExistingElement),
                    "Incorrect field element specified"
                )
            )
        );
    }
}