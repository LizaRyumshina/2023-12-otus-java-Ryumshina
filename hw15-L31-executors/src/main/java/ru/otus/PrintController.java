package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PrintController {
    private static final Logger logger = LoggerFactory.getLogger(PrintController.class);
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final String firstThreadName = "Thread 1";
    private static boolean isFirstThreadTurn = true;
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        var printer = new PrintController();

        executor.execute(() -> printer.action(firstThreadName));
        executor.execute(() -> printer.action("Thread 2"));

        executor.shutdown();

        logger.info("The main end");
    }

    void action(String threadName) {
        int value = 1;
        boolean isIncrement = true;

        while (!Thread.currentThread().isInterrupted()) {
            lock.writeLock().lock();
            try {
                if ((threadName.equals(firstThreadName) && isFirstThreadTurn)
                   ||(!threadName.equals(firstThreadName) && !isFirstThreadTurn)) {
                    logger.info("{}. My name is {}", value, threadName);
                    if (isIncrement) {
                        value++;
                    } else {
                        value--;
                    }
                    if (value == 10 || value == 1) {
                        isIncrement = !isIncrement;
                    }
                    isFirstThreadTurn = !isFirstThreadTurn;
                }
            } finally {
                lock.writeLock().unlock();
            }
            sleep();
        }
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
