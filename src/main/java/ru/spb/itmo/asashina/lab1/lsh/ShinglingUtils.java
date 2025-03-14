package ru.spb.itmo.asashina.lab1.lsh;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShinglingUtils {

    public static Set<String> buildShingles(String sentence, int k) {
        Set<String> shingles = new HashSet<>();
        int length = sentence.length();
        for (int i = 0; i < length - k; i++) {
            shingles.add(sentence.substring(i, i + k));
        }
        return shingles;
    }

    public static Map<String, Integer> buildVocabulary(List<Set<String>> shingleSets) {
        Set<String> fullSet = new HashSet<>();
        shingleSets.forEach(fullSet::addAll);

        Map<String, Integer> vocabulary = new HashMap<>();
        var index = 0;
        for (String shingle : fullSet) {
            vocabulary.put(shingle, index++);
        }
        return vocabulary;
    }

    public static double[] oneHot(Set<String> shingles, Map<String, Integer> vocabulary) {
        var vector = new double[vocabulary.size()];
        for (String shingle : shingles) {
            var index = vocabulary.get(shingle);
            if (index != null) {
                vector[index] = 1.0;
            }
        }
        return vector;
    }

}
