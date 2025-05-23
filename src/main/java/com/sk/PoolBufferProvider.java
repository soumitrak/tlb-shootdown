package com.sk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PoolBufferProvider implements ByteBufferProvider {

    private static final Logger logger = LogManager.getLogger(PoolBufferProvider.class);

    private final ConcurrentLinkedQueue<ByteBuffer> buffers = new ConcurrentLinkedQueue<>();
    private final Map<String, FileChannel> file2channel = new ConcurrentHashMap<>();
    private final Map<String, ByteBuffer> file2buffer = new ConcurrentHashMap<>();

    public void init(String fileName) throws Exception {
        FileChannel channel = FileChannel.open(Paths.get(fileName), StandardOpenOption.READ);
        file2channel.put(fileName, channel);
        ByteBuffer buffer = buffers.poll();
        if (buffer == null) {
            // logger.info("Creating buffer");
            buffer = ByteBuffer.allocate((int) Utils.PAGE_SIZE);
        } else {
            // logger.info("Reusing buffer");
        }
        file2buffer.put(fileName, buffer);
        channel.read(buffer, 0);
    }

    @Override
    public ByteBuffer get(String fileName, int index) {
        return file2buffer.get(fileName);
    }

    @Override
    public void close(String fileName) throws Exception {
        buffers.add(file2buffer.remove(fileName));
        file2channel.remove(fileName).close();
    }
}
