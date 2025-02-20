package ru.spb.itmo.asashina.lab1.ext.hash;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectoryTest {

    @Test
    void addValueTest() {
        var directory = new Directory<Integer>(3);

        assertDoesNotThrow(() -> directory.insert(1));

        var key = directory.getKey(1);
        assertEquals(0b1, key);
    }

    @Test
    void addValueWithRebalanceTest() {
        var directory = new Directory<Integer>(2);
        for (var i = 1; i <= 4; i++) {
            directory.insert(i);
        }

        assertDoesNotThrow(() -> directory.insert(5));

        var key = directory.getKey(5);
        assertEquals(0b01, key);
    }

}