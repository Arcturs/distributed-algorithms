package ru.spb.itmo.asashina.lab1.perf.hash;

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
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class GetValueBenchmarkRunner {

    private static final Random RANDOM = new Random();

    private Set<Long> data;
    private String currentBenchmark;
    private PerfectHashMap<Long, Integer> perfectHashMap;

    @Setup(Level.Iteration)
    public void setUp(BenchmarkParams params) {
        currentBenchmark = params.getBenchmark();
        List<Long> collection = new ArrayList<>();
        if (currentBenchmark.contains("Million")) {
            for (var i = 0; i < 1_000_000; i++) {
                collection.add(RANDOM.nextLong(Long.MAX_VALUE));
            }
        } else if (currentBenchmark.endsWith("Seven")) {
            for (var i = 0; i < 750_000; i++) {
                collection.add(RANDOM.nextLong(Long.MAX_VALUE));
            }
        } else {
            for (var i = 0; i < 250_000; i++) {
                collection.add(RANDOM.nextLong(Long.MAX_VALUE));
            }
        }
        perfectHashMap = new PerfectHashMap<>(new HashSet<>(collection), this::perfectLongHash);
        for (var key : collection) {
            perfectHashMap.insertValue(key, RANDOM.nextInt(Integer.MAX_VALUE));
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
    public void getTwoHundredThousandRandomData(Blackhole blackhole) {
        for (var key : data) {
            var value = perfectHashMap.getValue(key);
            blackhole.consume(value);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void getSevenHundredThousandRandomData(Blackhole blackhole) {
        for (var key : data) {
            var value = perfectHashMap.getValue(key);
            blackhole.consume(value);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void getMillionRandomData(Blackhole blackhole) {
        for (var key : data) {
            var value = perfectHashMap.getValue(key);
            blackhole.consume(value);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(GetValueBenchmarkRunner.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

    private int perfectLongHash(Long value) {
        var k = 0;
        var tempValue = value;
        while(tempValue / Integer.MAX_VALUE > 0) {
            k++;
            tempValue /= Integer.MAX_VALUE;
        }
        return Math.abs(value.hashCode()) + k;
    }

}
