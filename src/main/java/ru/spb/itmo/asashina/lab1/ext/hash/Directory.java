package ru.spb.itmo.asashina.lab1.ext.hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Directory<T> {

    private static final Logger log = Logger.getLogger(Directory.class.getName());
    private static final int MAX_GLOBAL_DEPTH = 32;

    private int globalDepth = 0;
    private Bucket<T>[] buckets;
    private int nextBucketPosition;

    private final long bucketCapacity;
    private final Map<T, Integer> valueToHashCodeCache = new HashMap<>();

    public Directory(long bucketCapacity) {
        this.bucketCapacity = bucketCapacity;
        buckets = new Bucket[] {new Bucket<T>(bucketCapacity), new Bucket<T>(bucketCapacity)};
        nextBucketPosition = buckets.length;
        globalDepth++;
    }

    public void insert(T value) {
        var valueHash = getHash(value);
        var key = getLastNBitsFromValueHash(valueHash, globalDepth);
        var bucket = getBucketByKey(key);
        var result = bucket.insert(value);
        if (result) {
            return;
        }

        if (bucket.getLocalDepth() < globalDepth) {
            addNewBucket(bucket, key);
        } else {
            rebalanceBuckets(bucket, key);
        }
        nextBucketPosition++;

        insert(value);
    }

    public Integer getKey(T value) {
        var valueHash = getHash(value);
        var key = getLastNBitsFromValueHash(valueHash, globalDepth);
        var bucket = getBucketByKey(key);
        if (bucket.exists(value)) {
            return key;
        }
        return null;
    }

    private int getHash(T value) {
        if (!valueToHashCodeCache.containsKey(value)) {
            valueToHashCodeCache.put(value, Math.abs(value.hashCode()));
        }
        return valueToHashCodeCache.get(value);
    }

    private Bucket<T> getBucketByKey(int key) {
        if (buckets[key] != null) {
            return buckets[key];
        }

        return buckets[getLastNBitsFromValueHash(key, globalDepth - 1)];
    }

    private void addNewBucket(Bucket<T> previousBucket, int previousBucketPosition) {
        var bucket = new Bucket<T>(bucketCapacity);
        var tempBucketStorage = previousBucket.getStorage();
        var oldBucketStorage = new ArrayList<T>();
        var newBucketStorage = new ArrayList<T>();

        for (var element : tempBucketStorage) {
            if (getLastNBitsFromValueHash(valueToHashCodeCache.get(element), globalDepth) == previousBucketPosition) {
                oldBucketStorage.add(element);
            } else {
                newBucketStorage.add(element);
            }
        }

        var newLocalDepth = previousBucket.getLocalDepth() + 1;
        previousBucket.setStorage(oldBucketStorage);
        previousBucket.setLocalDepth(newLocalDepth);
        bucket.setStorage(newBucketStorage);
        bucket.setLocalDepth(newLocalDepth);
        buckets[nextBucketPosition] = bucket;
    }

    private void rebalanceBuckets(Bucket<T> previousBucket, int previousBucketPosition) {
        if (globalDepth == MAX_GLOBAL_DEPTH) {
            throw new OutOfMemoryError("Reached maximum number of buckets");
        }
        var newBuckets = new Bucket[buckets.length * 2];
        System.arraycopy(buckets, 0, newBuckets, 0, buckets.length);
        buckets = newBuckets;
        globalDepth++;
        addNewBucket(previousBucket, previousBucketPosition);
    }

    private static int getLastNBitsFromValueHash(int hash, int n) {
        int mask = (1 << n) - 1;
        return hash & mask;
    }

}
