package com.sk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    static String prefix = null;
    static int numFiles = 0;
    static int numThreads = 1;
    static int numReads = 1;

    static void usage() {
        logger.info("Usage: -p <file name prefix> -nf <number of files> [-nt <number of threads>] [-nr <number of reads>]");
        System.exit(0);
    }

    static void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-p":
                    prefix = args[++i];
                    break;
                case "-nf":
                    numFiles = Integer.parseInt(args[++i]);
                    break;
                case "-nt":
                    numThreads = Integer.parseInt(args[++i]);
                    break;
                case "-nr":
                    numReads = Integer.parseInt(args[++i]);
                    break;
                default:
                    usage();
                    break;
            }
        }

        if (prefix == null || numFiles <= 0) usage();
    }

    public static void run(String[] args, ByteBufferProvider provider) throws Exception {
        parseArgs(args);
        var readers = new Readers(numThreads, prefix, numFiles, numReads, provider);
        readers.readAll();
    }
}
