package com.epam;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
//TODO: extract JVM options to properties
@BenchmarkMode(Mode.SingleShotTime)
@Warmup(iterations = 0)
@Timeout(time = 30, timeUnit = TimeUnit.MINUTES)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MINUTES)

@Measurement(iterations = 1)
public class RunBenchmark {

    private static BatchReadFilePidProcessorImpl batchReadFilePidProcessor;
    private static BufferedReadFilePidProcessor bufferedReadFilePidProcessor;

    @Setup
    public void prepareConection() throws IOException {
        batchReadFilePidProcessor = new BatchReadFilePidProcessorImpl();
        bufferedReadFilePidProcessor = new BufferedReadFilePidProcessor();
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Fork(jvmArgs = {"-Xms8g", "-Xmx8g", "-Xmn8g", "-XX:+UseG1GC"}, value = 1)
    public void findBencmark8gWithBuffer() throws IOException, URISyntaxException {
        findCountForEachPID("big_result_8g_buffered.txt", bufferedReadFilePidProcessor);
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Fork(jvmArgs = {"-Xms6g", "-Xmx6g", "-Xmn6g", "-XX:+UseG1GC"}, value = 1)
    public void findBencmark6gWithBuffer() throws IOException, URISyntaxException {
        findCountForEachPID("big_result_6g_buffered.txt", bufferedReadFilePidProcessor);
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Fork(jvmArgs = {"-Xms4g", "-Xmx4g", "-Xmn4g", "-XX:+UseG1GC"}, value = 1)
    public void findBencmark4gWithBuffer() throws IOException, URISyntaxException {
        findCountForEachPID("big_result_4g_buffered.txt", bufferedReadFilePidProcessor);
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Fork(jvmArgs = {"-Xms10g", "-Xmx10g", "-Xmn10g", "-XX:+UseG1GC"}, value = 1)
    public void findBencmark8gWithBatch() throws IOException, URISyntaxException {
        findCountForEachPID("big_result_8g_batch.txt", batchReadFilePidProcessor);
    }

    public void findCountForEachPID(final String finalName, final FilePidProcessor proccessor) throws IOException, URISyntaxException {
        Configuration configuration = new Configuration();
        configuration.set("io.compression.codecs", "org.apache.hadoop.io.compress.BZip2Codec");
        Path path;
        CompressionCodecFactory factory;
        FileSystem hdfs;
        factory = new CompressionCodecFactory(configuration);
        path = new Path(URI.create("hdfs://192.168.56.101:8020/user/hadoop/part1/raw/"));
        hdfs = FileSystem.get(path.toUri(), configuration);
        FileStatus[] status = hdfs.listStatus(path);
        Map<String, Long> pidCounts = null;
        for (FileStatus it : status) {
            CompressionCodec codec = factory.getCodec(it.getPath());
            InputStream stream = null;
            try {
                if (codec == null) {
                    continue;
                }
                stream = codec.createInputStream(hdfs.open(it.getPath()));

                pidCounts = proccessor.processBigFile(stream);
            } catch (IOException e) {
                System.out.println("ERROR: IOException");
            }
        }

        List<Map.Entry<String, Long>> entries = pidCounts.entrySet().stream().sorted((o1, o2) -> (int) (o2.getValue() - o1.getValue())).collect(Collectors.toList());
        FSDataOutputStream outputStream = hdfs.create(new Path(new URI("hdfs://192.168.56.101:8020/user/hadoop/" + finalName)));
        PrintWriter printWriter = new PrintWriter(outputStream);


        String collect = entries.stream().map(it -> it.getKey() + "\t" + it.getValue()).collect(Collectors.joining("\n"));
        printWriter.write(collect);
    }

}
