package ru.spb.itmo.asashina.lab1.perf.hash;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public class Bucket<T, V> {

    private static final Random RANDOM = new Random();
    private static final int P = 1_000_000_007;

    private final int size;
    private final V[] valueElements;
    private final Function<T, Integer> hashFunction;
    private final Map<T, Integer> keysToHashCodesCache = new HashMap<>();

    private int aCoeff;
    private int bCoeff;
    private T[] keyElements;

    public Bucket(Collection<T> keys, Function<T, Integer> hashFunction) {
        if (keys.isEmpty()) {
            throw new IllegalArgumentException("Bucket should contain at least one element");
        }

        this.hashFunction = hashFunction;
        this.size = keys.size() * keys.size();
        this.keyElements = (T[]) new Object[size];
        this.valueElements = (V[]) new Object[size];
        insertKeys(keys);
    }

    public boolean contains(T key) {
        int hash = getKeyHash(key);
        return keyElements[hash] != null;
    }

    public void insertValue(T key, V value) {
        if (contains(key)) {
            valueElements[getKeyHash(key)] = value;
        } else {
            throw new IllegalArgumentException("Bucket does not contain key: " + key);
        }
    }

    public V getValue(T key) {
        if (contains(key)) {
            return valueElements[getKeyHash(key)];
        }
        throw new IllegalArgumentException("Bucket does not contain key: " + key);
    }

    private void insertKeys(Collection<T> keys) {
        for (var key : keys) {
            int hash = getKeyHash(key);
            keyElements[hash] = key;
        }
    }

    /**
     * Использует формулу, которая была описана в источниках. Сохранена до лучших времен.
     * @param keys - ключи.
     */
    private void insertKeysV2(Collection<T> keys) {
        var hasCollision = true;
        while (hasCollision) {
            aCoeff = RANDOM.nextInt(P - 1);
            bCoeff = RANDOM.nextInt(P);

            var i = 0;
            for (var key : keys) {
                int hash = getKeyHash(key);
                if (keyElements[hash] == null) {
                    i++;
                    keyElements[hash] = key;
                    continue;
                }
                if (!keyElements[hash].equals(key)) {
                    break;
                }
                i++;
            }
            if (i == keys.size()) {
                hasCollision = false;
            } else {
                keyElements = (T[]) new Object[size];
            }
        }
    }

    private int getKeyHash(T key) {
        if (!keysToHashCodesCache.containsKey(key)) {
            keysToHashCodesCache.put(key, Math.abs(hashFunction.apply(key)) % size);
        }
        return keysToHashCodesCache.get(key);
    }

    /**
     * Использует формулу, которая была описана в источниках. Сохранена до лучших времен.
     * @param key - ключ.
     */
    private int getKeyHashV2(T key) {
        if (!keysToHashCodesCache.containsKey(key)) {
            keysToHashCodesCache.put(
                    key,
                    (int) ((((long) aCoeff * Math.abs(hashFunction.apply(key)) + bCoeff) % P) % size));
        }
        return keysToHashCodesCache.get(key);
    }

}
