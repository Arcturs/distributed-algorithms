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
public class LuceneIndexBenchmarkRunner {

    private static final String BENCHMARK_INDEX_PATH = "./indexes/benchmark";

    @Setup(Level.Invocation)
    public void setUp(BenchmarkParams params) throws IOException {
        FileUtils.deleteDirectory(new File(BENCHMARK_INDEX_PATH));
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void luceneIndexWithOneRow(Blackhole blackhole) {
        var searcher = new LuceneSearcher(BENCHMARK_INDEX_PATH, 1);
        blackhole.consume(searcher);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void luceneIndexWithOneHundredRows(Blackhole blackhole) {
        var searcher = new LuceneSearcher(BENCHMARK_INDEX_PATH, 100);
        blackhole.consume(searcher);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void luceneIndexWithFiveHundredRows(Blackhole blackhole) {
        var searcher = new LuceneSearcher(BENCHMARK_INDEX_PATH, 500);
        blackhole.consume(searcher);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void luceneIndexWithThousandRows(Blackhole blackhole) {
        var searcher = new LuceneSearcher(BENCHMARK_INDEX_PATH, 1_000);
        blackhole.consume(searcher);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void luceneIndexWithFiveThousandRows(Blackhole blackhole) {
        var searcher = new LuceneSearcher(BENCHMARK_INDEX_PATH, 5_000);
        blackhole.consume(searcher);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void luceneIndexWithTenThousandRows(Blackhole blackhole) {
        var searcher = new LuceneSearcher(BENCHMARK_INDEX_PATH, 10_000);
        blackhole.consume(searcher);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void luceneIndexWithSixteenThousandRows(Blackhole blackhole) {
        var searcher = new LuceneSearcher(BENCHMARK_INDEX_PATH, 16_000);
        blackhole.consume(searcher);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(LuceneIndexBenchmarkRunner.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
