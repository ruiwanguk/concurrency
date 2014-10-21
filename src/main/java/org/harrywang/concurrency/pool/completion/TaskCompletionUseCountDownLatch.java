package org.harrywang.concurrency.pool.completion;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Check task completion using CountDownLatch
 *
 * @author Rui Wang
 * @version $Id$
 */
public class TaskCompletionUseCountDownLatch {

    public static void main(String[] args) throws IOException, InterruptedException {

        // init a fixed sized thread pool with 10 threads
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // counter
        CountDownLatch countDownLatch = new CountDownLatch(20);
        final AtomicLong counter = new AtomicLong();

        try {
            // add 20 tasks to the thread pool
            start(executorService, counter, countDownLatch);

            // wait until all have finished
            countDownLatch.await();
        } finally {
            executorService.shutdown();
        }

        System.out.println("Total number of counts is " + counter + " and should be " + 1000);
    }

    private static void start(ExecutorService executorService,
                                      final AtomicLong counter,
                                      final CountDownLatch countDownLatch) {
        for (int i = 0; i < 20; i++) {
            Future<?> result = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 50; i++) {
                            counter.incrementAndGet();

                            TimeUnit.SECONDS.sleep(1);
                        }
                        countDownLatch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
