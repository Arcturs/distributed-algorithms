package ru.spb.itmo.asashina.lab2.ball.tree;

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
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@State(Scope.Thread)
public class BallTreeCreateBenchmarkRunner {

    private static final Random RANDOM = new Random();

    private List<int[]> data;
    private String currentBenchmark;

    @Setup(Level.Iteration)
    public void setUp(BenchmarkParams params) {
        currentBenchmark = params.getBenchmark();
        data = new ArrayList<>();
        if (currentBenchmark.contains("OneRandom")) {
            for (var i = 0; i < 1; i++) {
                data.add(
                        IntStream.of(0, 3)
                                .map(ignored -> RANDOM.nextInt(10))
                                .toArray());
            }
        } else if (currentBenchmark.contains("Fifty")) {
            for (var i = 0; i < 50; i++) {
                data.add(
                        IntStream.of(0, 10)
                                .map(ignored -> RANDOM.nextInt(10))
                                .toArray());
            }
        } else if (currentBenchmark.contains("OneThousand")) {
            for (var i = 0; i < 1_000; i++) {
                data.add(
                        IntStream.of(0, 25)
                                .map(ignored -> RANDOM.nextInt(10))
                                .toArray());
            }
        } else if (currentBenchmark.contains("Five")) {
            for (var i = 0; i < 5_000; i++) {
                data.add(
                        IntStream.of(0, 40)
                                .map(ignored -> RANDOM.nextInt(10))
                                .toArray());
            }
        } else {
            for (var i = 0; i < 15_000; i++) {
                data.add(
                        IntStream.of(0, 75)
                                .map(ignored -> RANDOM.nextInt(10))
                                .toArray());
            }
        }
        Collections.shuffle(data);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void createTreeWithOneRandomData(Blackhole blackhole) {
        var ballTree = new BallTree(1, 1, data);
        blackhole.consume(ballTree);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void createTreeWithFiftyRandomData(Blackhole blackhole) {
        var ballTree = new BallTree(3, 10, data);
        blackhole.consume(ballTree);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void createTreeWithOneThousandRandomData(Blackhole blackhole) {
        var ballTree = new BallTree(20, 80, data);
        blackhole.consume(ballTree);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void createTreeWithFiveThousandRandomData(Blackhole blackhole) {
        var ballTree = new BallTree(30, 200, data);
        blackhole.consume(ballTree);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void createTreeWithFifteenThousandRandomData(Blackhole blackhole) {
        var ballTree = new BallTree(50, 450, data);
        blackhole.consume(ballTree);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BallTreeCreateBenchmarkRunner.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
