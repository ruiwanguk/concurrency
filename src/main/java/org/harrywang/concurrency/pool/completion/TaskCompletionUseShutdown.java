package org.harrywang.concurrency.pool.completion;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Check task completion using thread pool shutdown
 *
 * @author Rui Wang
 * @version $Id$
 */
public class TaskCompletionUseShutdown {
    public static void main(String[] args) throws IOException, InterruptedException {

        // init a fixed sized thread pool with 10 threads
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // counter
        final AtomicLong counter = new AtomicLong();

        try {
            // add 20 tasks to the thread pool
            start(executorService, counter);
        } finally {
            executorService.shutdown();
        }

        System.out.println("Total number of counts is " + counter + " and should be " + 1000);
    }

    private static void start(ExecutorService executorService, final AtomicLong counter) throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    // code here cannot throw InterruptedException,
                    // otherwise, the thread will be terminated.
                    for (int i = 0; i < 50; i++) {
                        counter.incrementAndGet();
                    }
                }
            });

            TimeUnit.SECONDS.sleep(5);
        }

    }
}
