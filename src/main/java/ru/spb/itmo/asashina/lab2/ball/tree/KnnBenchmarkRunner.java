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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@State(Scope.Thread)
public class KnnBenchmarkRunner {

    private static final Random RANDOM = new Random();

    private Knn knn;
    private List<int[]> data;
    private String currentBenchmark;

    @Setup(Level.Iteration)
    public void setUp(BenchmarkParams params) {
        currentBenchmark = params.getBenchmark();
        data = new ArrayList<>();
        if (currentBenchmark.contains("TwoRandom")) {
            for (var i = 0; i < 2; i++) {
                data.add(
                        IntStream.of(0, 3)
                                .map(ignored -> RANDOM.nextInt(10))
                                .toArray());
            }
            knn = new Knn(data, 1, 1);
        } else if (currentBenchmark.contains("Fifty")) {
            readCsvFile("resources/fashion-mnist_test.csv", 50);
            knn = new Knn(data, 10, 3);
        } else if (currentBenchmark.contains("TwoHundred")) {
            readCsvFile("resources/fashion-mnist_test.csv", 200);
            knn = new Knn(data, 20, 10);
        } else {
            readCsvFile("resources/fashion-mnist_test.csv", 1_000);
            knn = new Knn(data, 25, 15);
        }
        Collections.shuffle(data);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void knnGetOneWithTwoRandomData(Blackhole blackhole) {
        var result = knn.getKNeighbours(data.get(0));
        blackhole.consume(result);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void knnGetAllWithTwoRandomData(Blackhole blackhole) {
        for (var x : data) {
            var result = knn.getKNeighbours(x);
            blackhole.consume(result);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void knnGetOneWithFiftyRandomData(Blackhole blackhole) {
        var result = knn.getKNeighbours(data.get(0));
        blackhole.consume(result);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void knnGetAllWithFiftyRandomData(Blackhole blackhole) {
        for (var x : data) {
            var result = knn.getKNeighbours(x);
            blackhole.consume(result);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void knnGetOneWithTwoHundredRandomData(Blackhole blackhole) {
        var result = knn.getKNeighbours(data.get(0));
        blackhole.consume(result);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void knnGetAllWithTwoHundredRandomData(Blackhole blackhole) {
        for (var x : data) {
            var result = knn.getKNeighbours(x);
            blackhole.consume(result);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void knnGetOneWithOneThousandRandomData(Blackhole blackhole) {
        var result = knn.getKNeighbours(data.get(0));
        blackhole.consume(result);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void knnGetAllWithOneThousandRandomData(Blackhole blackhole) {
        for (var x : data) {
            var result = knn.getKNeighbours(x);
            blackhole.consume(result);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(KnnBenchmarkRunner.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

    private void readCsvFile(String fileName, int rowAmount) {
        boolean isFirstLine = true;
        var size = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] columns = line.split(",");
                if (columns.length <= 1) continue;

                int[] rowData = new int[columns.length - 1];
                for (int i = 1; i < columns.length; i++) {
                    rowData[i - 1] = Integer.parseInt(columns[i].trim());
                }
                data.add(rowData);
                size++;
                if (size == rowAmount) {
                    break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

}
