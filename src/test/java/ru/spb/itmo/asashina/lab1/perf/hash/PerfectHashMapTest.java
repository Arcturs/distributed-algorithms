package ru.spb.itmo.asashina.lab1.perf.hash;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PerfectHashMapTest {

    @Test
    void createMapWithoutCollisionsTest() {
        var perfectHashMap = new PerfectHashMap<>(Set.of(1, 2, 3, 4), Object::hashCode);

        assertEquals(1, perfectHashMap.getKeyNumber(1));
        assertEquals(2, perfectHashMap.getKeyNumber(2));
        assertEquals(3, perfectHashMap.getKeyNumber(3));
        assertEquals(0, perfectHashMap.getKeyNumber(4));
        assertNull(perfectHashMap.getKeyNumber(21));
    }

    @Test
    void createMapWithCollisionsTest() {
        var perfectHashMap = new PerfectHashMap<>(
                Set.of(1L, (long) Integer.MAX_VALUE * 2 + 1, (long) Integer.MAX_VALUE * 4 + 7),
                this::perfectLongHash);

        assertEquals(1, 1L);
        assertEquals(1, perfectHashMap.getKeyNumber((long) Integer.MAX_VALUE * 2 + 1));
        assertEquals(1, perfectHashMap.getKeyNumber((long) Integer.MAX_VALUE * 4 + 7));
    }

    @Test
    void insertValuesToMapTest() {
        var perfectHashMap = new PerfectHashMap<Integer, Integer>(Set.of(1, 2, 3, 4), Object::hashCode);
        perfectHashMap.insertValue(1, 10);
        perfectHashMap.insertValue(3, 30);

        assertEquals(10, perfectHashMap.getValue(1));
        assertEquals(30, perfectHashMap.getValue(3));
    }

    @Test
    void insertValuesToMapWithCollisionsTest() {
        var perfectHashMap = new PerfectHashMap<Long, Integer>(
                Set.of(1L, (long) Integer.MAX_VALUE * 2 + 1, (long) Integer.MAX_VALUE * 4 + 7),
                this::perfectLongHash);
        perfectHashMap.insertValue(1L, 10);
        perfectHashMap.insertValue((long) Integer.MAX_VALUE * 2 + 1, 30);

        assertEquals(10, perfectHashMap.getValue(1L));
        assertEquals(30, perfectHashMap.getValue((long) Integer.MAX_VALUE * 2 + 1));
    }

    private int perfectLongHash(Long value) {
        var k = 0;
        var tempValue = value;
        while(tempValue / Integer.MAX_VALUE > 0) {
            k++;
            tempValue /= Integer.MAX_VALUE;
        }
        return value.hashCode() + k;
    }

}