package ru.spb.itmo.asashina.lab1.lsh;

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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class LSHBenchmarkRunner {

    private static final EasyRandom EASY_RANDOM = new EasyRandom(
            new EasyRandomParameters()
                    .stringLengthRange(10, 100)
                    .seed(new Date().getTime()));

    private Set<String> data;

    private String currentBenchmark;

    @Setup(Level.Iteration)
    public void setUp(BenchmarkParams params) {
        currentBenchmark = params.getBenchmark();
        data = new HashSet<>();
        if (currentBenchmark.contains("Seven")) {
            for (var i = 0; i < 750; i++) {
                data.add(EASY_RANDOM.nextObject(String.class));
            }
        } else if (currentBenchmark.endsWith("Five")) {
            for (var i = 0; i < 500; i++) {
                data.add(EASY_RANDOM.nextObject(String.class));
            }
        } else {
            for (var i = 0; i < 250; i++) {
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
    public void insertSevenHundredRandomData(Blackhole blackhole) {
        var processor = new SimilarSentencesProcessor(data, 8, 100, 20);
        blackhole.consume(processor);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void insertFiveHundredRandomData(Blackhole blackhole) {
        var processor = new SimilarSentencesProcessor(data, 8, 100, 20);
        blackhole.consume(processor);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void insertTwoHundredRandomData(Blackhole blackhole) {
        var processor = new SimilarSentencesProcessor(data, 8, 100, 20);
        blackhole.consume(processor);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(LSHBenchmarkRunner.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
