package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PrintController {
    private static final Logger logger = LoggerFactory.getLogger(PrintController.class);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private String secondThreadName = "Thread 2";

    public static void main(String[] args) {

        var printer = new PrintController();
        var thread1 = new Thread(() -> printer.action(printer.getSecondThreadName()));
        thread1.setName("Thread 1");
        var thread2 = new Thread(() -> printer.action("Thread 1"));
        thread2.setName(printer.getSecondThreadName());
        thread1.start();
        thread2.start();

        logger.info("The main end");
    }

    private void action(String nextThreadName) {
        int value = 1;
        boolean isIncrement = true;

        while (!Thread.currentThread().isInterrupted()) {
            lock.writeLock().lock();
            try {
                if (!nextThreadName.equals(secondThreadName)) {
                    logger.info("{}. My name is {}", value, nextThreadName);
                    if (isIncrement) {
                        value++;
                    } else {
                        value--;
                    }
                    if (value == 10 || value == 1) {
                        isIncrement = !isIncrement;
                    }
                    secondThreadName = nextThreadName;
                }
            } finally {
                lock.writeLock().unlock();
            }
            sleep();
        }
    }
    private String getSecondThreadName() {
        return secondThreadName;
    }
    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
