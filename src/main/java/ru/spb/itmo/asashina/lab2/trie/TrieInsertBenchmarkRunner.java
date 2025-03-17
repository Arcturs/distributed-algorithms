package ru.spb.itmo.asashina.lab2.trie;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
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
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class TrieInsertBenchmarkRunner {

    private static final EasyRandom EASY_RANDOM = new EasyRandom(
            new EasyRandomParameters()
                    .stringLengthRange(10, 1_000)
                    .seed(new Date().getTime()));

    private Trie trie;
    private Set<String> data;
    private String currentBenchmark;

    @Setup(Level.Iteration)
    public void setUp(BenchmarkParams params) {
        currentBenchmark = params.getBenchmark();
        trie = new Trie();
        data = new HashSet<>();
        if (currentBenchmark.contains("One")) {
            for (var i = 0; i < 1; i++) {
                data.add(EASY_RANDOM.nextObject(String.class));
            }
        } else if (currentBenchmark.endsWith("Hundred")) {
            for (var i = 0; i < 10_000; i++) {
                data.add(EASY_RANDOM.nextObject(String.class));
            }
        } else if (currentBenchmark.endsWith("Five")) {
            for (var i = 0; i < 50_000; i++) {
                data.add(EASY_RANDOM.nextObject(String.class));
            }
        } else {
            for (var i = 0; i < 100_000; i++) {
                data.add(EASY_RANDOM.nextObject(String.class));
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void insertOneRandomData() {
        for (var sentence : data) {
            trie.insert(sentence);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void insertTenThousandRandomData() {
        for (var sentence : data) {
            trie.insert(sentence);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void insertFiftyThousandRandomData() {
        for (var sentence : data) {
            trie.insert(sentence);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void insertOneHundredThousandRandomData() {
        for (var sentence : data) {
            trie.insert(sentence);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(TrieInsertBenchmarkRunner.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
