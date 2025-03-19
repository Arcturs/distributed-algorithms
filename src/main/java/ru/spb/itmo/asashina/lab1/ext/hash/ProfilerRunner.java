package ru.spb.itmo.asashina.lab1.ext.hash;

import java.util.Random;

public class ProfilerRunner {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        var directory = new Directory<>(10_000);
        for (int i = 0; i < 100_000; i++) {
            directory.insert(RANDOM.nextInt(Integer.MAX_VALUE));
        }
    }

}
