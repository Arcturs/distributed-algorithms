package ru.spb.itmo.asashina.lab1.lsh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public class MinHashUtils {

    private final static Random RANDOM = new Random();

    public static int[][] buildMinHashArray(Map<String, Integer> vocabulary, int resolution) {
        int length = vocabulary.size();
        int[][] arr = new int[resolution][length];

        for (int i = 0; i < resolution; i++) {
            List<Integer> permutation = new ArrayList<>();
            IntStream.range(0, length)
                    .forEach(permutation::add);
            Collections.shuffle(permutation, RANDOM);
            for (int j = 0; j < length; j++) {
                arr[i][j] = permutation.get(j) + 1;
            }
        }
        return arr;
    }

    public static int[] getSignature(int[][] minhash, double[] vector) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] == 1.0) {
                indices.add(i);
            }
        }
        if (indices.isEmpty()) {
            throw new IllegalArgumentException("Vector contains only zeros");
        }
        int[] signature = new int[minhash.length];
        for (int i = 0; i < minhash.length; i++) {
            int min = Integer.MAX_VALUE;
            for (int idx : indices) {
                if (minhash[i][idx] < min) {
                    min = minhash[i][idx];
                }
            }
            signature[i] = min;
        }
        return signature;
    }

}
