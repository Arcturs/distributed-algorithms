package ru.spb.itmo.asashina.lab1.ext.hash;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

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
    public void insertFiveMillionsValues(Blackhole blackhole) {
        directory = new Directory<>(5_000_000);
        for (int i = 0; i < 5_000_000; i++) {
            directory.insert(RANDOM.nextInt(Integer.MAX_VALUE));
            blackhole.consume(i);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(InsertBenchmarkRunner.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
