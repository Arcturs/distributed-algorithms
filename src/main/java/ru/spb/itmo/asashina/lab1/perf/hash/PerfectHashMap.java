package ru.spb.itmo.asashina.lab1.perf.hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class PerfectHashMap<T> {

    private Bucket<T>[] buckets;
    private Function<T, Integer> hashFunction;

    public PerfectHashMap(Set<T> keys, Function<T, Integer> hashFunction) {
        if (keys.isEmpty()) {
            throw new IllegalArgumentException("HashMap should contain at least one element");
        }
        this.hashFunction = hashFunction;
        this.buckets = new Bucket[keys.size()];
        insertKeys(keys);
    }

    public Integer getKeyNumber(T key) {
        int hash = calculateKeyHash(key);
        if (buckets[hash] != null && buckets[hash].contains(key)) {
            return hash;
        }
        return null;
    }

    private void insertKeys(Set<T> keys) {
        var hashToKeys = new HashMap<Integer, List<T>>();
        for (var key : keys) {
            int hash = calculateKeyHash(key);
            var previousKeys = hashToKeys.getOrDefault(hash, new ArrayList<>());
            previousKeys.add(key);
            hashToKeys.put(hash, previousKeys);
        }

        for (var hash : hashToKeys.keySet()) {
            buckets[hash] = new Bucket(hashToKeys.get(hash), hashFunction);
        }
    }

    private int calculateKeyHash(T key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

}
