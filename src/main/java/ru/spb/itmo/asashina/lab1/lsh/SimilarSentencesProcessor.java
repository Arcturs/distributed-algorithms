package ru.spb.itmo.asashina.lab1.lsh;

import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ru.spb.itmo.asashina.lab1.lsh.MinHashUtils.buildMinHashArray;
import static ru.spb.itmo.asashina.lab1.lsh.MinHashUtils.getSignature;
import static ru.spb.itmo.asashina.lab1.lsh.ShinglingUtils.buildShingles;
import static ru.spb.itmo.asashina.lab1.lsh.ShinglingUtils.buildVocabulary;
import static ru.spb.itmo.asashina.lab1.lsh.ShinglingUtils.oneHot;

public class SimilarSentencesProcessor {

    private static final int DEFAULT_K_VALUE = 2;
    private static final int DEFAULT_BANDS_VALUE = 5;
    private static final int DEFAULT_RESOLUTION_VALUE = 10;

    private final Set<String> sentences;
    private final List<Pair<Integer, Integer>> candidateList = new ArrayList<>();

    private int k = DEFAULT_K_VALUE;
    private int bands = DEFAULT_BANDS_VALUE;
    private int resolution = DEFAULT_RESOLUTION_VALUE;

    public SimilarSentencesProcessor(Set<String> sentences) {
        this.sentences = sentences;
        findCandidates();
    }

    public SimilarSentencesProcessor(Set<String> sentences, int k, int resolution, int bands) {
        this.sentences = sentences;
        this.k = k;
        this.resolution = resolution;
        this.bands = bands;
        findCandidates();
    }

    public List<Pair<Integer, Integer>> getFirstNSimilarSentences(int n) {
        return candidateList.subList(0, Math.min(n, candidateList.size()));
    }

    private void findCandidates() {
        List<Set<String>> shingles = new ArrayList<>();
        for (String sentence : sentences) {
            shingles.add(buildShingles(sentence, k));
        }
        Map<String, Integer> vocabulary = buildVocabulary(shingles);
        double[][] shinglesArray = new double[shingles.size()][vocabulary.size()];
        for (int i = 0; i < shingles.size(); i++) {
            shinglesArray[i] = oneHot(shingles.get(i), vocabulary);
        }

        int[][] minhashArray = buildMinHashArray(vocabulary, resolution);
        int[][] signatures = new int[shinglesArray.length][minhashArray.length];
        for (int i = 0; i < shinglesArray.length; i++) {
            signatures[i] = getSignature(minhashArray, shinglesArray[i]);
        }

        LSH lsh = new LSH(bands);
        for (int[] signature : signatures) {
            lsh.addHash(signature);
        }
        Set<Pair<Integer, Integer>> candidatePairs = lsh.checkCandidates();
        candidateList.addAll(candidatePairs);
    }

}
