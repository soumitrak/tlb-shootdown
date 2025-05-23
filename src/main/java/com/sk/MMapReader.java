package com.sk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MMapReader {
    private static final Logger logger = LogManager.getLogger(MMapReader.class);

    public static void main(String[] args) throws Exception {
        var provider = new MMapBufferProvider();
        App.run(args, provider);
    }
}
