package com.sk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;

public class Reader implements Callable<Integer> {
    private static final Logger logger = LogManager.getLogger(Reader.class);

    private final String fileName;
    private final int numReads;
    private final ByteBufferProvider byteBufferProvider;

    public Reader(String fileName, int numReads, ByteBufferProvider byteBufferProvider) {
        this.fileName = fileName;
        this.numReads = numReads;
        this.byteBufferProvider = byteBufferProvider;
    }

    @Override
    public Integer call() throws Exception {
        try {
            return callInternal();
        } catch (Exception e) {
            logger.error("Caught exception: ", e);
            throw e;
        }
    }

    private int callInternal() throws Exception {
        Random random = new Random();
        byteBufferProvider.init(fileName);

        logger.debug("Starting to read {} values", numReads);
        long startTime = System.nanoTime();
        long prev = startTime;
        int i;
        for (i = 0; i < numReads; i++) {
            int randomIndex = random.nextInt(Utils.ENTRIES_PER_PAGE);
            ByteBuffer buffer = byteBufferProvider.get(fileName, randomIndex);
            int base = Utils.base(randomIndex);
            int len = buffer.getInt(base);
            byte[] ba = new byte[len];
            buffer.get(base + 4, ba);
            String str = new String(ba, StandardCharsets.UTF_8);
            UUID uuid = Utils.generateUUIDFromLong(randomIndex);
            if (!uuid.toString().equals(str)) {
                logger.error("Invalid UUID found for int {} at base: {} read: {} should be: {}", randomIndex, base, str, uuid);
            }
/*
            if (i > 0 && i % 1000 == 0) {
                long curr = System.nanoTime();
                long duration = curr - prev;
                double micros = ((double) duration / 1000.0) / 1000;
                logger.info("Finished reading {} values rate: {} us/call", i, micros);
                prev = curr;
            }
 */
        }

        long duration = System.nanoTime() - startTime;
        double micros = ((double) duration / i) / 1000;
        logger.debug("Finished reading all values ({}) rate: {} us/call", i, micros);

        byteBufferProvider.close(fileName);

        return i;
    }
}
