package ru.spb.itmo.asashina.lab3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LuceneSearcherTest {

    @TempDir
    private File testIndexFile;

    @Test
    void indexWhenDirectoryDoesNotExistTest() {
        assertDoesNotThrow(() -> new LuceneSearcher(testIndexFile.getPath(), 2));

        assertEquals(5, testIndexFile.listFiles().length);
    }

    @Test
    void indexWhenDirectoryExistsTest() {
        new LuceneSearcher(testIndexFile.getPath(), 2);
        assertDoesNotThrow(() -> new LuceneSearcher(testIndexFile.getPath(), 2));

        assertEquals(5, testIndexFile.listFiles().length);
    }

    @Test
    void searchTest() {
        var searcher = new LuceneSearcher(testIndexFile.getPath(), 10);

        var result = searcher.search(new RequestQuery("genres:\"Comedy\" AND title:\"Shrek *\"", 1));

        assertEquals(1, result.size());
        assertEquals("Shrek Forever After", result.get(0).getField("title").stringValue());
    }

}