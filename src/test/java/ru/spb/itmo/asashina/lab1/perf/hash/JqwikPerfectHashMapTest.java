package ru.spb.itmo.asashina.lab1.perf.hash;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.Positive;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JqwikPerfectHashMapTest {

    @Property
    void createMapWithoutCollisionsTest(
            @ForAll @Positive int value1,
            @ForAll @Positive int value2,
            @ForAll @Positive int value3) {

        var set = new HashSet<>(List.of(value1, value2, value3));

        var perfectHashMap = new PerfectHashMap<>(set, Object::hashCode);

        assertTrue(set.stream().allMatch(val -> perfectHashMap.getKeyNumber(val) != null));
    }

    @Property
    void createMapWithCollisionsTest(
            @ForAll @Positive long value1,
            @ForAll @Positive long value2,
            @ForAll @Positive long value3) {

        var set = new HashSet<>(List.of(value1, value2, value3));

        var perfectHashMap = new PerfectHashMap<>(set, this::perfectLongHash);

        assertTrue(set.stream().allMatch(val -> perfectHashMap.getKeyNumber(val) != null));
    }

    private int perfectLongHash(Long value) {
        var k = 0;
        var tempValue = value;
        while (tempValue / Integer.MAX_VALUE > 0) {
            k++;
            tempValue /= Integer.MAX_VALUE;
        }
        return value.hashCode() + k;
    }

}
