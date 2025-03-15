package ru.spb.itmo.asashina.lab1.ext.hash;

import org.apache.commons.io.FileUtils;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.util.logging.Level.FINE;

public class Directory<T> {

    public static final String PARENT_DIRECTORY = "buckets";

    private static final Logger log = Logger.getLogger(Directory.class.getName());
    private static final int MAX_GLOBAL_DEPTH = 32;

    private int globalDepth = 0;
    private Bucket<T>[] buckets;
    private int nextBucketPosition;

    private final long bucketCapacity;
    private final Map<String, Integer> valueToHashCodeCache = new HashMap<>();

    public Directory(long bucketCapacity) {
        this.bucketCapacity = bucketCapacity;
        buckets = new Bucket[] { new Bucket<T>(bucketCapacity, 0), new Bucket<T>(bucketCapacity, 1) };
        nextBucketPosition = buckets.length;
        globalDepth++;
        try {
            Files.createDirectories(Path.of(PARENT_DIRECTORY));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(T value) {
        var valueHash = getHash(value);
        var key = getLastNBitsFromValueHash(valueHash, globalDepth);
        var bucket = getBucketByKey(key);
        if (bucket == null) {
            throw new RuntimeException("Bucket with key does not exist, something is wrong");
        }
        var result = bucket.insert(value, key);
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
        if (!valueToHashCodeCache.containsKey(value.toString())) {
            valueToHashCodeCache.put(value.toString(), Math.abs(value.hashCode()));
        }
        return valueToHashCodeCache.get(value.toString());
    }

    private Bucket<T> getBucketByKey(int key) {
        if (buckets[key] != null) {
            return buckets[key];
        }

        var tempDepth = globalDepth - 1;
        while(tempDepth != 0) {
            if (buckets[getLastNBitsFromValueHash(key, tempDepth)] != null) {
                return buckets[getLastNBitsFromValueHash(key, tempDepth)];
            }
            tempDepth--;
        }
        return null;
    }

    private void addNewBucket(Bucket<T> previousBucket, int previousBucketPosition) {
        var previousBucketFileName = previousBucket.getFileName();
        Bucket<T> newBucket = new Bucket<>(bucketCapacity);
        Path tempFile;
        Path readFile;
        File targetFile;
        try {
            tempFile = Files.createFile(Path.of(previousBucketFileName.replace("bucket.dat", "bucket-tmp.dat")));
            readFile = Files.createFile(Path.of(previousBucketFileName.replace("bucket.dat", "bucket-read.dat")));
            targetFile = FileUtils.getFile(previousBucketFileName);
            FileUtils.copyFile(targetFile, readFile.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var newLocalDepth = previousBucket.getLocalDepth() + 1;
        previousBucket.setLocalDepth(newLocalDepth);
        newBucket.setLocalDepth(newLocalDepth);

        try (
                DataInputStream dis = new DataInputStream(new FileInputStream(readFile.toString()));
                DataOutputStream dos = new DataOutputStream(
                        new BufferedOutputStream(Files.newOutputStream(Path.of(tempFile.toString()), APPEND)))) {

            while (true) {
                var remainingSize = dis.readLong();
                var element = dis.readUTF();

                var newKey = getLastNBitsFromValueHash(valueToHashCodeCache.get(element), globalDepth);
                if (newKey == previousBucketPosition || newKey == previousBucket.getId()) {
                    previousBucket.insert(dos, element, newKey);
                } else {
                    newBucket.insert(element, newKey);
                }

                if (remainingSize == 1) {
                    break;
                }
            }
            Files.move(tempFile, FileUtils.getFile(previousBucketFileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.delete(readFile);
        } catch (EOFException e) {
            log.log(FINE,"Reading is over");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        buckets[nextBucketPosition] = newBucket;
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
