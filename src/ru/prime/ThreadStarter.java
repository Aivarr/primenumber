package ru.prime;

import java.util.concurrent.Semaphore;

public class ThreadStarter {

    private static final String RESULT_PATH = "result.txt";

    public static void main(String[] args) {

        CommonResource commonResource = new CommonResource(RESULT_PATH);
        Semaphore semaphore = new Semaphore(1, true);

        PrimeThread thread1 = new PrimeThread("tread1.txt", commonResource, semaphore);
        PrimeThread thread2 = new PrimeThread("tread2.txt", commonResource, semaphore);

        thread1.start();
        thread2.start();
    }
}
