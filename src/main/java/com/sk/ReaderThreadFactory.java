package com.sk;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ReaderThreadFactory implements ThreadFactory {
    private final String threadNamePrefix;
    private final AtomicInteger threadCounter = new AtomicInteger(1);

    public ReaderThreadFactory(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(threadNamePrefix + threadCounter.getAndIncrement());
        return thread;
    }
}
