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

    private int perfectLongHash(Long value) {
        var k = 0;
        var tempValue = value;
        while(tempValue / Integer.MAX_VALUE > 0) {
            k++;
            tempValue /= Integer.MAX_VALUE;
        }
        return (int) value.hashCode() + k;
    }

}