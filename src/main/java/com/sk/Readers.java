package com.sk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Readers {
    private static final Logger logger = LogManager.getLogger(Readers.class);

    private static final String THREAD_NAME_PREFIX = "reader-";

    private final int numThreads;
    private final String filePrefix;
    private final int numFiles;
    private final int numReads;
    private final ByteBufferProvider bufferProvider;

    public Readers(int numThreads, String filePrefix, int numFiles, int numReads, ByteBufferProvider bufferProvider) {
        this.numThreads = numThreads;
        this.filePrefix = filePrefix;
        this.numFiles = numFiles;
        this.numReads = numReads;
        this.bufferProvider = bufferProvider;
    }

    public void readAll() throws InterruptedException {
        long startTime = System.nanoTime();
        logger.info("Starting to read {} files in {} threads", numFiles, numThreads);
        var es = Executors.newFixedThreadPool(numThreads, new ReaderThreadFactory(THREAD_NAME_PREFIX));

        for (int i = 0; i < numFiles; i++) {
            String filename = filePrefix + i;
            es.submit(new Reader(filename, numReads, bufferProvider));
        }

        es.shutdown();
        logger.info("Waiting 10 hours for all tasks to complete.");
        es.awaitTermination(10, TimeUnit.HOURS);

        logger.info("Finished reading all files in {} millis", ((double)System.nanoTime() - startTime) / 1000000);
    }
}
