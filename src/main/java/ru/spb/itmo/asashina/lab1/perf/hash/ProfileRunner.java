package ru.spb.itmo.asashina.lab1.perf.hash;

import java.util.HashSet;
import java.util.Random;

public class ProfileRunner {

    public static void main(String[] args) {
        var random = new Random();
        var data = new HashSet<Long>();
        for (var i = 0; i < 1_000_000; i++) {
            data.add(random.nextLong(Long.MAX_VALUE));
        }

        var perfectHashMap = new PerfectHashMap<>(data, ProfileRunner::perfectLongHash);
        System.out.println(perfectHashMap.getKeyNumber(1L));
    }

    private static int perfectLongHash(Long value) {
        var k = 0;
        var tempValue = value;
        while(tempValue / Integer.MAX_VALUE > 0) {
            k++;
            tempValue /= Integer.MAX_VALUE;
        }
        return (int) (value.hashCode() + k);
    }

}
