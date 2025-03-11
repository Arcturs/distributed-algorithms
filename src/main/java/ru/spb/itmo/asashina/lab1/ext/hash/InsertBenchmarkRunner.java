package ru.spb.itmo.asashina.lab1.ext.hash;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class InsertBenchmarkRunner {

    private static final Random RANDOM = new Random();

    private Directory<Integer> directory;

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void insertTenThousandValues(Blackhole blackhole) {
        directory = new Directory<>(10_000);
        for (int i = 0; i < 10_000; i++) {
            directory.insert(RANDOM.nextInt(Integer.MAX_VALUE));
            blackhole.consume(i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void insertMillionValues(Blackhole blackhole) {
        directory = new Directory<>(1_000_000);
        for (int i = 0; i < 1_000_000; i++) {
            directory.insert(RANDOM.nextInt(Integer.MAX_VALUE));
            blackhole.consume(i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void insertTenMillionsValues(Blackhole blackhole) {
        directory = new Directory<>(10_000_000);
        for (int i = 0; i < 10_000_000; i++) {
            directory.insert(RANDOM.nextInt(Integer.MAX_VALUE));
            blackhole.consume(i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void insertHundredMillionValues(Blackhole blackhole) {
        directory = new Directory<>(100_000_000);
        for (int i = 0; i < 100_000_000; i++) {
            directory.insert(RANDOM.nextInt(Integer.MAX_VALUE));
            blackhole.consume(i);
        }
    }

    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }

}
