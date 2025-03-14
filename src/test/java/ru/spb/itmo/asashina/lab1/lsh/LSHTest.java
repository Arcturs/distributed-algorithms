package ru.spb.itmo.asashina.lab1.lsh;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LSHTest {

    @Test
    void findSimilarSentencesTest() {
        var sentences = Set.of(
                "Children in red shirts are playing in the leaves",
                "Two young women are sparring in a kickboxing fight",
                "Two young women are not sparring in a kickboxing fight"
        );

        var processor = new SimilarSentencesProcessor(sentences);

        var result = processor.getFirstNSimilarSentences(2);
        assertEquals(1, result.size());
    }

    @Test
    void findCompletelyDifferentSentencesTest() {
        var sentences = Set.of(
                "Children in red shirts are playing in the leaves",
                "Two young women are sparring in a kickboxing fight"
        );

        var processor = new SimilarSentencesProcessor(sentences);

        var result = processor.getFirstNSimilarSentences(2);
        assertEquals(0, result.size());
    }

}
