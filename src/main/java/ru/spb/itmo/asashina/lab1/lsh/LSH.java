package ru.spb.itmo.asashina.lab1.lsh;

import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LSH {

    private final int bandsNumber;
    private final List<Map<String, List<Integer>>> buckets;
    private int counter;

    public LSH(int bandsNumber) {
        this.bandsNumber = bandsNumber;
        this.buckets = new ArrayList<>();
        for (int i = 0; i < bandsNumber; i++) {
            buckets.add(new HashMap<>());
        }
        this.counter = 0;
    }

    public List<int[]> makeSubvectors(int[] signature) {
        int length = signature.length;
        if (length % bandsNumber != 0) {
            throw new IllegalArgumentException("Invalid signature length");
        }
        int r = length / bandsNumber;
        List<int[]> subvectors = new ArrayList<>();
        for (int i = 0; i < length; i += r) {
            subvectors.add(Arrays.copyOfRange(signature, i, Math.min(i + r, length)));
        }
        return subvectors;
    }

    public void addHash(int[] signature) {
        List<int[]> subvectors = makeSubvectors(signature);
        for (int i = 0; i < subvectors.size(); i++) {
            String key = Arrays.stream(subvectors.get(i))
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(","));
            Map<String, List<Integer>> band = buckets.get(i);
            band.computeIfAbsent(key, k -> new ArrayList<>())
                    .add(counter);
        }
        counter++;
    }

    public Set<Pair<Integer, Integer>> checkCandidates() {
        Set<Pair<Integer, Integer>> candidates = new HashSet<>();
        for (Map<String, List<Integer>> band : buckets) {
            for (List<Integer> hits : band.values()) {
                if (hits.size() > 1) {
                    for (int i = 0; i < hits.size(); i++) {
                        for (int j = i + 1; j < hits.size(); j++) {
                            candidates.add(new Pair<>(hits.get(i), hits.get(j)));
                        }
                    }
                }
            }
        }
        return candidates;
    }

}
