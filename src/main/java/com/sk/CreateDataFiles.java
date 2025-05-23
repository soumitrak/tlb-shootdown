package com.sk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class CreateDataFiles {

    private static final Logger logger = LogManager.getLogger(CreateDataFiles.class);

    static String prefix = null;
    static int numFiles = 0;

    static void usage() {
        logger.info("Usage: -p <file name prefix> -nf <number of files>");
        System.exit(0);
    }

    static void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-p": prefix = args[++i]; break;
                case "-nf": numFiles = Integer.parseInt(args[++i]); break;
                default: usage(); break;
            }
        }

        if (prefix == null || numFiles <= 0) usage();
    }

    static void writeDataFile(final int i) throws Exception {
        String fn = prefix + i;
        logger.info("Creating {} file to store the UUIDs", fn);
        RandomAccessFile file = new RandomAccessFile(fn, "rw");
        FileChannel channel = file.getChannel();

        int numPage = 0;
        long offset = 0;
        MappedByteBuffer buffer = null;

        for (int e = 0; e < Utils.ENTRIES_PER_PAGE; e++) {
            var ba = Utils.generateUUIDFromLong(e).toString().getBytes();
            // If there are not enough space left in the current page to write the array,
            // extend the file and add a buffer.
            if (offset + 4 + ba.length > numPage * Utils.PAGE_SIZE) {
                // Extend the file by one page
                numPage++;
                file.setLength(numPage * Utils.PAGE_SIZE);
                offset = (numPage - 1) * Utils.PAGE_SIZE;
                buffer = channel.map(FileChannel.MapMode.READ_WRITE, offset, Utils.PAGE_SIZE);
            }

            // Add the data
            buffer.putInt(ba.length);
            buffer.put(ba);
            offset += 4 + ba.length;
        }

        file.close();
    }

    public static void main(String[] args) throws Exception {
        parseArgs(args);
        for (int i = 0; i < numFiles; i++) {
            writeDataFile(i);
        }
    }
}
