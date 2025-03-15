package ru.spb.itmo.asashina.lab1.lsh;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ProfileRunner {

    private static final EasyRandom EASY_RANDOM = new EasyRandom(
            new EasyRandomParameters()
                    .stringLengthRange(10, 300)
                    .seed(new Date().getTime()));

    public static void main(String[] args) {
        Set<String> data = new HashSet<>();
        for (var i = 0; i < 1_000; i++) {
            data.add(EASY_RANDOM.nextObject(String.class));
        }
        var processor = new SimilarSentencesProcessor(data, 8, 100, 20);
        System.out.println(processor.getFirstNSimilarSentences(1));
    }

}
