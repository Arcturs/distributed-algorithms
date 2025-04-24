package ru.spb.itmo.asashina.lab3;

import org.apache.commons.io.FileUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class LuceneSearchBenchmarkRunner {

    private static final String BENCHMARK_INDEX_PATH = "./indexes/benchmark";

    private String currentBenchmark;
    private LuceneSearcher searcher;

    @Setup(Level.Trial)
    public void setUp(BenchmarkParams params) throws IOException {
        currentBenchmark = params.getBenchmark();
        FileUtils.deleteDirectory(new File(BENCHMARK_INDEX_PATH));
        if (currentBenchmark.contains("SixteenThousand")) {
            searcher = new LuceneSearcher(BENCHMARK_INDEX_PATH, 16_000);
        } else if (currentBenchmark.contains("TenThousand")) {
            searcher = new LuceneSearcher(BENCHMARK_INDEX_PATH, 10_000);
        } else if (currentBenchmark.contains("FiveThousand")) {
            searcher = new LuceneSearcher(BENCHMARK_INDEX_PATH, 5_000);
        } else if (currentBenchmark.contains("Thousand")) {
            searcher = new LuceneSearcher(BENCHMARK_INDEX_PATH, 1_000);
        } else if (currentBenchmark.contains("FiveHundred")) {
            searcher = new LuceneSearcher(BENCHMARK_INDEX_PATH, 500);
        } else if (currentBenchmark.contains("Hundred")) {
            searcher = new LuceneSearcher(BENCHMARK_INDEX_PATH, 100);
        } else {
            searcher = new LuceneSearcher(BENCHMARK_INDEX_PATH, 1);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void luceneSearchWithOneRow(Blackhole blackhole) {
        var result = searcher.search(new RequestQuery("genres:\"Comedy\"", 10));
        blackhole.consume(result);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void luceneSearchWithOneHundredRows(Blackhole blackhole) {
        var result = searcher.search(new RequestQuery("genres:\"Comedy\"", 10));
        blackhole.consume(result);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void luceneSearchWithFiveHundredRows(Blackhole blackhole) {
        var result = searcher.search(new RequestQuery("genres:\"Comedy\"", 10));
        blackhole.consume(result);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void luceneSearchWithThousandRows(Blackhole blackhole) {
        var result = searcher.search(new RequestQuery("genres:\"Comedy\"", 10));
        blackhole.consume(result);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void luceneSearchWithFiveThousandRows(Blackhole blackhole) {
        var result = searcher.search(new RequestQuery("genres:\"Comedy\"", 10));
        blackhole.consume(result);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void luceneSearchWithTenThousandRows(Blackhole blackhole) {
        var result = searcher.search(new RequestQuery("genres:\"Comedy\"", 10));
        blackhole.consume(result);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void luceneSearchWithSixteenThousandRows(Blackhole blackhole) {
        var result = searcher.search(new RequestQuery("genres:\"Comedy\"", 10));
        blackhole.consume(result);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(LuceneSearchBenchmarkRunner.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
