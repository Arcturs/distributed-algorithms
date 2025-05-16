package ru.spb.itmo.asashina.lab4;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class RGADeleteBenchmarkRunner {

    private int lettersNum = 1;
    private Document document;
    private String currentBenchmark;

    @Setup(Level.Iteration)
    public void setUp(BenchmarkParams params) throws IOException {
        currentBenchmark = params.getBenchmark();
        document = new Document(1);
        if (currentBenchmark.contains("OneLetter")) {
            lettersNum = 1;
        } else if (currentBenchmark.contains("OneHundred")) {
            lettersNum = 100;
        } else if (currentBenchmark.contains("FiveHundred")) {
            lettersNum = 500;
        } else if (currentBenchmark.contains("OneThousand")) {
            lettersNum = 1_000;
        } else {
            lettersNum = 5_000;
        }
        for (var i = 0; i < lettersNum; i++) {
            document.insert('a');
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void deleteOneLetter() {
        for (int i = lettersNum; i >= 0; i--) {
            document.delete(i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void deleteOneHundredLetter() {
        for (int i = lettersNum; i >= 0; i--) {
            document.delete(i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void deleteFiveHundredLetter() {
        for (int i = lettersNum; i >= 0; i--) {
            document.delete(i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void deleteOneThousandLetter() {
        for (int i = lettersNum; i >= 0; i--) {
            document.delete(i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void deleteFiveThousandLetter() {
        for (int i = lettersNum; i >= 0; i--) {
            document.delete(i);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(RGADeleteBenchmarkRunner.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
    
}
