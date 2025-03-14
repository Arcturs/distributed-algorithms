package ru.spb.itmo.asashina.lab1.ext.hash;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.Positive;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JqwikDirectoryTest {

    @Property
    void addValueTest(@ForAll @Positive int value) {
        var directory = new Directory<Integer>(3);

        assertDoesNotThrow(() -> directory.insert(value));

        var key = directory.getKey(value);
        assertNotNull(key);
    }

    @Property
    void addValueWithRebalanceTest(@ForAll @Positive int value) {
        var directory = new Directory<Integer>(2);
        for (var i = 1; i <= 4; i++) {
            directory.insert(i);
        }

        assertDoesNotThrow(() -> directory.insert(value));

        var key = directory.getKey(value);
        assertNotNull(key);
    }

}
