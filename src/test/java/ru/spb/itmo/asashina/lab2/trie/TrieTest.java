package ru.spb.itmo.asashina.lab2.trie;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.NotBlank;
import net.jqwik.api.constraints.StringLength;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrieTest {

    @Property
    void insertWordTest(@ForAll @NotBlank @StringLength(min = 2, max = 30) String word) {
        var trie = new Trie();

        assertDoesNotThrow(() -> trie.insert(word));
    }

    @Property
    void insertWordsTest(
            @ForAll @NotBlank @StringLength(min = 2, max = 30) String word1,
            @ForAll @NotBlank @StringLength(min = 2, max = 30) String word2,
            @ForAll @NotBlank @StringLength(min = 2, max = 30) String word3,
            @ForAll @NotBlank @StringLength(min = 2, max = 30) String word4,
            @ForAll @NotBlank @StringLength(min = 2, max = 30) String word5) {

        var trie = new Trie();

        assertDoesNotThrow(
                () -> {
                    trie.insert(word1);
                    trie.insert(word2);
                    trie.insert(word3);
                    trie.insert(word4);
                    trie.insert(word5);
                }
        );
    }

    @Property
    void deleteWordTest(@ForAll @NotBlank @StringLength(min = 2, max = 30) String word) {
        var trie = new Trie();
        trie.insert(word);

        var result = trie.delete(word);

        assertTrue(result);
    }

    @Test
    void deleteWordsTest() {
        var trie = new Trie();
        trie.insert("abc");
        trie.insert("def");

        var result = trie.delete("abc");
        assertTrue(result);

        result = trie.delete("def");
        assertTrue(result);
    }

    @Test
    void deleteWordsWithCommonPrefixTest() {
        var trie = new Trie();
        trie.insert("ac");
        trie.insert("ab");

        var result = trie.delete("ab");
        assertTrue(result);

        result = trie.delete("ac");
        assertFalse(result);
    }

    @Property
    void containsWordTest(@ForAll @NotBlank @StringLength(min = 2, max = 30) String word) {
        var trie = new Trie();
        trie.insert(word);

        var result = trie.containsWord(word);

        assertTrue(result);
    }

    @Property
    void containsWordsTest(
            @ForAll @NotBlank @StringLength(min = 2, max = 30) String word1,
            @ForAll @NotBlank @StringLength(min = 2, max = 30) String word2,
            @ForAll @NotBlank @StringLength(min = 2, max = 30) String word3) {

        var trie = new Trie();
        trie.insert(word1);
        trie.insert(word3);

        var result = trie.containsWord(word1);
        assertTrue(result);

        result = trie.containsWord(word2);
        assertFalse(result);

        result = trie.containsWord(word3);
        assertTrue(result);
    }

    @Property
    void containsPrefixTest(@ForAll @NotBlank @StringLength(min = 5, max = 30) String word) {
        var trie = new Trie();
        trie.insert(word);

        var result = trie.startsWith(word.substring(0, word.length() - 2));

        assertTrue(result);
    }

}