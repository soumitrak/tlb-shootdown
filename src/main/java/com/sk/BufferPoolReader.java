package com.sk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BufferPoolReader {
    private static final Logger logger = LogManager.getLogger(BufferPoolReader.class);

    public static void main(String[] args) throws Exception {
        var provider = new PoolBufferProvider();
        App.run(args, provider);
    }
}
