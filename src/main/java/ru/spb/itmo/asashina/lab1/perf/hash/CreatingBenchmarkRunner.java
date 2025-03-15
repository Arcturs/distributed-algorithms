package ru.spb.itmo.asashina.lab1.perf.hash;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class CreatingBenchmarkRunner {

    private static final Random RANDOM = new Random();

    private final Set<Long> data = new HashSet<>();

    private String currentBenchmark;

    @Setup(Level.Iteration)
    public void setUp(BenchmarkParams params) {
        currentBenchmark = params.getBenchmark();
        if (currentBenchmark.contains("Million")) {
            for (var i = 0; i < 1_000_000; i++) {
                data.add(RANDOM.nextLong(Long.MAX_VALUE));
            }
        } else if (currentBenchmark.endsWith("Five")) {
            for (var i = 0; i < 500_000; i++) {
                data.add(RANDOM.nextLong(Long.MAX_VALUE));
            }
        } else {
            for (var i = 0; i < 100_000; i++) {
                data.add(RANDOM.nextLong(Long.MAX_VALUE));
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void insertHundredThousandRandomData(Blackhole blackhole) {
        var perfectHashMap = new PerfectHashMap<>(data, this::perfectLongHash);
        blackhole.consume(perfectHashMap);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void insertFiveHundredThousandRandomData(Blackhole blackhole) {
        var perfectHashMap = new PerfectHashMap<>(data, this::perfectLongHash);
        blackhole.consume(perfectHashMap);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void insertMillionRandomData(Blackhole blackhole) {
        var perfectHashMap = new PerfectHashMap<>(data, this::perfectLongHash);
        blackhole.consume(perfectHashMap);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CreatingBenchmarkRunner.class.getSimpleName())
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
        return value.hashCode() + k;
    }

}
