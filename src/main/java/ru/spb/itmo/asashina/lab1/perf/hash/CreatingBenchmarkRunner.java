package ru.spb.itmo.asashina.lab1.perf.hash;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class CreatingBenchmarkRunner {

    private static final Random RANDOM = new Random();

    private final Set<Long> data = new HashSet<>();

    @Setup(Level.Iteration)
    public void setUp() {
        for (var i = 0; i < 1_000_000; i++) {
            data.add(RANDOM.nextLong(Long.MAX_VALUE));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void insertRandomData(Blackhole blackhole) {
        var perfectHashMap = new PerfectHashMap<>(data, this::perfectLongHash);
        blackhole.consume(perfectHashMap);
    }

    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }

    private int perfectLongHash(Long value) {
        var k = 0;
        var tempValue = value;
        while(tempValue / Integer.MAX_VALUE > 0) {
            k++;
            tempValue /= Integer.MAX_VALUE;
        }
        return (int) (value.hashCode() + k);
    }

}
