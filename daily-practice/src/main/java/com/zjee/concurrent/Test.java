package com.zjee.concurrent;

public class Test {
    public synchronized static void main(String[] args) throws InterruptedException {

        Test.class.wait();
    }
}
