package ru.spb.itmo.asashina.lab1.perf.hash;

import java.util.*;
import java.util.function.Function;

public class PerfectHashMap<T, V> {

    private final Bucket<T, V>[] buckets;
    private final Function<T, Integer> hashFunction;
    private final Map<T, Integer> keysToHashCodesCache = new HashMap<>();

    public PerfectHashMap(Set<T> keys, Function<T, Integer> hashFunction) {
        if (keys.isEmpty()) {
            throw new IllegalArgumentException("HashMap should contain at least one element");
        }
        this.hashFunction = hashFunction;
        this.buckets = new Bucket[keys.size()];
        insertKeys(keys);
    }

    public Integer getKeyNumber(T key) {
        int hash = getKeyHash(key);
        if (buckets[hash] != null && buckets[hash].contains(key)) {
            return hash;
        }
        return null;
    }

    public void insertValue(T key, V value) {
        int hash = getKeyHash(key);
        if (buckets[hash] != null) {
            buckets[hash].insertValue(key, value);
        } else {
            throw new IllegalArgumentException("Map does not contain key " + key);
        }
    }

    public V getValue(T key) {
        int hash = getKeyHash(key);
        if (buckets[hash] != null) {
            return buckets[hash].getValue(key);
        }
        throw new IllegalArgumentException("Map does not contain key " + key);
    }

    private void insertKeys(Set<T> keys) {
        var hashToKeys = new HashMap<Integer, List<T>>();
        for (var key : keys) {
            int hash = getKeyHash(key);
            var previousKeys = hashToKeys.getOrDefault(hash, new ArrayList<>());
            previousKeys.add(key);
            hashToKeys.put(hash, previousKeys);
        }

        for (var hash : hashToKeys.keySet()) {
            buckets[hash] = new Bucket(hashToKeys.get(hash), hashFunction);
        }
    }

    private int getKeyHash(T key) {
        if (!keysToHashCodesCache.containsKey(key)) {
            keysToHashCodesCache.put(key, Math.abs(key.hashCode()) % buckets.length);
        }
        return keysToHashCodesCache.get(key);
    }

}
