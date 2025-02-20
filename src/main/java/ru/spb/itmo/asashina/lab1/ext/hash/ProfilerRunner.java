package ru.spb.itmo.asashina.lab1.ext.hash;

public class ProfilerRunner {

    public static void main(String[] args) {
        var directory = new Directory<>(100_000_000);
        for (int i = 0; i < 100_000_000; i++) {
            directory.insert(i);
        }
    }

}
