package ru.spb.itmo.asashina.lab2.trie;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.util.Date;

public class TrieInsertProfilerRunner {

    private static final EasyRandom EASY_RANDOM = new EasyRandom(
            new EasyRandomParameters()
                    .stringLengthRange(10, 1_000)
                    .seed(new Date().getTime()));

    public static void main(String[] args) {
        var trie = new Trie();
        for (int i = 0; i < 15_000; i++) {
            trie.insert(EASY_RANDOM.nextObject(String.class));
        }
    }

}
