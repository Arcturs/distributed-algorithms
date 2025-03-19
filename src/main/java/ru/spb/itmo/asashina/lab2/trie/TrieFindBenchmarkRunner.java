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
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class TrieFindBenchmarkRunner {

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
        List<String> collection = new ArrayList<>();
        if (currentBenchmark.contains("OneRandom")) {
            for (var i = 0; i < 1; i++) {
                var value = EASY_RANDOM.nextObject(String.class);
                collection.add(value);
                trie.insert(value);
            }
        } else if (currentBenchmark.contains("Fifty")) {
            for (var i = 0; i < 50; i++) {
                var value = EASY_RANDOM.nextObject(String.class);
                collection.add(value);
                trie.insert(value);
            }
        } else if (currentBenchmark.contains("OneThousand")) {
            for (var i = 0; i < 1_000; i++) {
                var value = EASY_RANDOM.nextObject(String.class);
                collection.add(value);
                trie.insert(value);
            }
        } else if (currentBenchmark.contains("Five")) {
            for (var i = 0; i < 5_000; i++) {
                var value = EASY_RANDOM.nextObject(String.class);
                collection.add(value);
                trie.insert(value);
            }
        } else {
            for (var i = 0; i < 15_000; i++) {
                var value = EASY_RANDOM.nextObject(String.class);
                collection.add(value);
                trie.insert(value);
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
    public void getOneRandomData(Blackhole blackhole) {
        for (var sentence : data) {
            var result = trie.containsWord(sentence);
            blackhole.consume(result);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void getFiftyRandomData(Blackhole blackhole) {
        for (var sentence : data) {
            var result = trie.containsWord(sentence);
            blackhole.consume(result);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void getOneThousandRandomData(Blackhole blackhole) {
        for (var sentence : data) {
            var result = trie.containsWord(sentence);
            blackhole.consume(result);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void getFiveThousandRandomData(Blackhole blackhole) {
        for (var sentence : data) {
            var result = trie.containsWord(sentence);
            blackhole.consume(result);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void getFifteenThousandRandomData(Blackhole blackhole) {
        for (var sentence : data) {
            var result = trie.containsWord(sentence);
            blackhole.consume(result);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(TrieFindBenchmarkRunner.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
