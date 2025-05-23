package com.sk;

import java.nio.ByteBuffer;

public interface ByteBufferProvider {
    void init(String fileName) throws Exception;
    ByteBuffer get(String fileName, int index);
    void close(String fileName) throws Exception;
}
