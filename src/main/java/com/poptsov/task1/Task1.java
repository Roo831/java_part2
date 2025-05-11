package com.poptsov.task1;

public class Task1 {

    private static final int MAX_COUNT = 10;
    private static final Object lock = new Object();
    private static boolean isOdd = true;

    public static void task1() {
        Thread evenThread = new Thread(() -> {
            for (int i = 2; i <= MAX_COUNT; i += 2) {
                synchronized (lock) {
                    while (isOdd) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.println("Чётный поток: " + i);
                    isOdd = true;
                    lock.notify();
                }
            }
        });

        Thread oddThread = new Thread(() -> {
            for (int i = 1; i <= MAX_COUNT; i += 2) {
                synchronized (lock) {
                    while (!isOdd) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.println("Нечётный поток: " + i);
                    isOdd = false;
                    lock.notify();
                }
            }
        });

        oddThread.start();
        evenThread.start();
    }
}
