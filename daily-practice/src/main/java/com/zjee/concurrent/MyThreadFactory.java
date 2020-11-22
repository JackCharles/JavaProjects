package com.zjee.concurrent;

import java.util.concurrent.ThreadFactory;

/**
 * @author zhongjie
 */
public class MyThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        return null;
    }
}
