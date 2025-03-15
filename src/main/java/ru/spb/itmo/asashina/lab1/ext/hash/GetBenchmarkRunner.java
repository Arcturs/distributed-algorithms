package ru.spb.itmo.asashina.lab1.ext.hash;

import org.apache.commons.io.FileUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static ru.spb.itmo.asashina.lab1.ext.hash.Directory.PARENT_DIRECTORY;

@State(Scope.Benchmark)
public class GetBenchmarkRunner {

    private static final Random RANDOM = new Random();

    private Directory<Integer> directory;
    private Set<Integer> data;
    private String currentBenchmark;

    @Setup(Level.Invocation)
    public void setUp(BenchmarkParams params) {
        try {
            FileUtils.deleteDirectory(FileUtils.getFile(PARENT_DIRECTORY));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        currentBenchmark = params.getBenchmark();
        List<Integer> collection = new ArrayList<>();
        if (currentBenchmark.contains("Five")) {
            directory = new Directory<>(1_000);
            for (var i = 0; i < 5_000; i++) {
                var value = RANDOM.nextInt(Integer.MAX_VALUE);
                collection.add(value);
                directory.insert(value);
            }
        } else if (currentBenchmark.endsWith("Thousand")) {
            directory = new Directory<>(75);
            for (var i = 0; i < 1_000; i++) {
                var value = RANDOM.nextInt(Integer.MAX_VALUE);
                collection.add(value);
                directory.insert(value);
            }
        } else {
            directory = new Directory<>(50);
            for (var i = 0; i < 750; i++) {
                var value = RANDOM.nextInt(Integer.MAX_VALUE);
                collection.add(value);
                directory.insert(value);
            }
        }
        Collections.shuffle(collection);
        data = new HashSet<>(collection);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void getThousandValues(Blackhole blackhole) {
        for (var value : data) {
            var key = directory.getKey(value);
            blackhole.consume(key);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void getFiveThousandValues(Blackhole blackhole) {
        for (var value : data) {
            var key = directory.getKey(value);
            blackhole.consume(key);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void getSevenHundredValues(Blackhole blackhole) {
        for (var value : data) {
            var key = directory.getKey(value);
            blackhole.consume(key);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(GetBenchmarkRunner.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
