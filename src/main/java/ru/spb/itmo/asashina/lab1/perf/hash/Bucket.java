package ru.spb.itmo.asashina.lab1.perf.hash;

import java.util.Collection;
import java.util.Random;
import java.util.function.Function;

public class Bucket<T> {

    private static final Random RANDOM = new Random();
    private static final int P = 1_000_000_007;

    private final int size;

    private int aCoeff;
    private int bCoeff;
    private T[] elements;
    private Function<T, Integer> hashFunction;

    public Bucket(Collection<T> keys, Function<T, Integer> hashFunction) {
        if (keys.isEmpty()) {
            throw new IllegalArgumentException("Bucket should contain at least one element");
        }

        this.hashFunction = hashFunction;
        this.size = keys.size() * keys.size();
        this.elements = (T[]) new Object[size];
        insertKeys(keys);
    }

    public boolean contains(T key) {
        int hash = calculateKeyHash(key);
        return key.equals(elements[hash]);
    }

    private void insertKeys(Collection<T> keys) {
        for (var key : keys) {
            int hash = calculateKeyHash(key);
            elements[hash] = key;
        }

//        var hasCollision = true;
//        while (hasCollision) {
//            aCoeff = RANDOM.nextInt(P - 1);
//            bCoeff = RANDOM.nextInt(P);
//
//            var i = 0;
//            for (var key : keys) {
//                int hash = calculateKeyHash(key);
//                if (elements[hash] == null) {
//                    i++;
//                    elements[hash] = key;
//                    continue;
//                }
//                if (!elements[hash].equals(key)) {
//                    break;
//                }
//                i++;
//            }
//            if (i == keys.size()) {
//                hasCollision = false;
//            } else {
//                elements = (T[]) new Object[size];
//            }
//        }
    }

    private int calculateKeyHash(T key) {
//        return (int) ((((long) aCoeff * Math.abs(hashFunction.apply(key)) + bCoeff) % P) % size);
        return Math.abs(hashFunction.apply(key)) % size;
    }

}
