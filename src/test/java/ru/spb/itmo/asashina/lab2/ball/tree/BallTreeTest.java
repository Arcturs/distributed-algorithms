package ru.spb.itmo.asashina.lab2.ball.tree;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BallTreeTest {

    @Test
    void createTreeTest() {
        assertDoesNotThrow(
                () -> new BallTree(
                        5,
                        20,
                        IntStream.range(0, 100)
                                .mapToObj(i -> new int[]{i + 1, i + 2})
                                .toList()));
    }

}