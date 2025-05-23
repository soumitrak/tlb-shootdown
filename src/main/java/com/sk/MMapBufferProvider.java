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
import java.util.concurrent.ConcurrentMap;

public class MMapBufferProvider implements ByteBufferProvider {

    private static final Logger logger = LogManager.getLogger(MMapBufferProvider.class);

    private final Map<String, FileChannel> file2channel = new ConcurrentHashMap<>();
    private final Map<String, ByteBuffer> file2buffer = new ConcurrentHashMap<>();

    @Override
    public void init(String fileName) throws Exception {
        FileChannel channel = FileChannel.open(Paths.get(fileName), StandardOpenOption.READ);
        file2channel.put(fileName, channel);
        file2buffer.put(fileName, channel.map(FileChannel.MapMode.READ_ONLY, 0, Utils.PAGE_SIZE));
    }

    @Override
    public ByteBuffer get(String fileName, int index) {
        return file2buffer.get(fileName);
    }

    @Override
    public void close(String fileName) throws Exception {
        file2buffer.remove(fileName);
        file2channel.remove(fileName).close();
    }
}
