package ru.spb.itmo.asashina.lab1.ext.hash;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Bucket<T> {

    private static final Logger log = Logger.getLogger(Bucket.class.getName());

    private final long capacity;

    private long localDepth = 0;
    private long remainingSize;
    private List<T> storage = new ArrayList<>();

    public Bucket(long capacity) {
        this.capacity = capacity;
        this.remainingSize = capacity;
        localDepth++;
    }

    public long getLocalDepth() {
        return localDepth;
    }

    public void setLocalDepth(long localDepth) {
        this.localDepth = localDepth;
    }

    public List<T> getStorage() {
        return storage;
    }

    public void setStorage(List<T> storage) {
        this.storage = storage;
        remainingSize = capacity;
    }

    public boolean insert(T element) {
        if (remainingSize - 1 >= 0) {
            storage.add(element);
            remainingSize--;
            return true;
        }

        return false;
    }

    public boolean exists(T element) {
        return storage.contains(element);
    }

}
