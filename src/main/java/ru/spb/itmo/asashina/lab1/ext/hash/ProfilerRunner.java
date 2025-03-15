package ru.spb.itmo.asashina.lab1.ext.hash;

// TODO: Обновить бенчмарки
public class ProfilerRunner {

    public static void main(String[] args) {
        var directory = new Directory<>(10_000);
        for (int i = 0; i < 500_000; i++) {
            directory.insert(i);
        }
    }

}
