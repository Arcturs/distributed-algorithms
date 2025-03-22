package ru.spb.itmo.asashina.lab2.ball.tree;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KnnTest {

    @Test
    void getKNeighborsWhenLeafSizeLessThanPointsTest() {
        var x1 = new int[]{2, 2};
        var x2 = new int[]{9, 3};
        var searchedX = new int[]{5, 5};
        var x3 = new int[]{6, 6};
        var x4 = new int[]{4, 7};
        var x = List.of(x1, x2, searchedX, x3, x4);

        var classifier = new Knn(x, 1, 2);

        var result = classifier.getKNeighbours(searchedX);
        assertEquals(2, result.size());
        assertTrue(result.contains(x3));
        assertTrue(result.contains(x4));
    }

    @Test
    void getKNeighborsWhenLeafSizeMoreThanPointsTest() {
        var x1 = new int[]{2, 2};
        var x2 = new int[]{9, 3};
        var searchedX = new int[]{5, 5};
        var x3 = new int[]{6, 6};
        var x4 = new int[]{4, 7};
        var x = List.of(x1, x2, searchedX, x3, x4);

        var classifier = new Knn(x, 6, 2);

        var result = classifier.getKNeighbours(searchedX);
        assertEquals(2, result.size());
        assertTrue(result.contains(x3));
        assertTrue(result.contains(x4));
    }

}