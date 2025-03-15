package ru.spb.itmo.asashina.lab1.ext.hash;

import org.apache.commons.io.FileUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static ru.spb.itmo.asashina.lab1.ext.hash.Directory.PARENT_DIRECTORY;

@State(Scope.Benchmark)
public class InsertBenchmarkRunner {

    private static final Random RANDOM = new Random();

    private Directory<Integer> directory;

    @Setup(Level.Invocation)
    public void setUp(BenchmarkParams params) {
        try {
            FileUtils.deleteDirectory(FileUtils.getFile(PARENT_DIRECTORY));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void insertThousandThousandValues(Blackhole blackhole) {
        directory = new Directory<>(75);
        for (int i = 0; i < 1_000; i++) {
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
    public void insertFiveThousandValues(Blackhole blackhole) {
        directory = new Directory<>(1_000);
        for (int i = 0; i < 5_000; i++) {
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
    public void insertFifteenThousandValues(Blackhole blackhole) {
        directory = new Directory<>(3_000);
        for (int i = 0; i < 15_000; i++) {
            directory.insert(RANDOM.nextInt(Integer.MAX_VALUE - 1));
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
