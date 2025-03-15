package ru.spb.itmo.asashina.lab1.ext.hash;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.Positive;
import net.jqwik.api.lifecycle.AfterTry;
import net.jqwik.api.lifecycle.BeforeTry;
import org.apache.commons.io.FileUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.spb.itmo.asashina.lab1.ext.hash.Directory.PARENT_DIRECTORY;

public class JqwikDirectoryTest {

    @AfterTry
    @BeforeTry
    void tearDown() {
        try {
            FileUtils.deleteDirectory(FileUtils.getFile(PARENT_DIRECTORY));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
