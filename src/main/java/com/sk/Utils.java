package com.sk;

import java.nio.ByteBuffer;
import java.util.UUID;

public class Utils {
    public static final long PAGE_SIZE = 4L * (1 << 10);
    public static final int BYTES_PER_ENTRY = 40; // 4 (string length) + 36 (byte array)
    public static final int ENTRIES_PER_PAGE = (int)PAGE_SIZE / BYTES_PER_ENTRY;

    public static UUID generateUUIDFromLong(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(value);
        byte[] longBytes = buffer.array();
        return UUID.nameUUIDFromBytes(longBytes);
    }

    // i and page number both starts from 0
    public static int pageNumber(int i) {
        return (int) Math.floor((double) i / Utils.ENTRIES_PER_PAGE);
    }

    public static int base(int i) {
        return (i % Utils.ENTRIES_PER_PAGE) * Utils.BYTES_PER_ENTRY;
    }
}
