package org.harrywang.concurrency.pool.completion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Check task completion using future
 *
 * @author Rui Wang
 * @version $Id$
 */
public class TaskCompletionUseFuture {

    public static void main(String[] args) throws IOException, InterruptedException {

        // init a fixed sized thread pool with 10 threads
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // counter
        final AtomicLong counter = new AtomicLong();

        try {
            // add 20 tasks to the thread pool
            List<Future> trackers = start(executorService, counter);

            // wait until all have finished
            monitor(trackers);
        } finally {
            executorService.shutdown();
        }

        System.out.println("Total number of counts is " + counter + " and should be " + 1000);
    }

    private static List<Future> start(ExecutorService executorService, final AtomicLong counter) {
        List<Future> trackers = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Future<?> result = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 50; i++) {
                            counter.incrementAndGet();

                            TimeUnit.SECONDS.sleep(1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            trackers.add(result);
        }
        return trackers;
    }

    private static void monitor(List<Future> trackers) throws InterruptedException {
        for (;;) {
            Iterator<Future> iterator = trackers.iterator();
            while (iterator.hasNext()) {
                Future result = iterator.next();
                if (result.isDone()) {
                    System.out.println("Thread finished");
                    iterator.remove();
                }
            }

            if (trackers.size() == 0) {
                break;
            }

            TimeUnit.SECONDS.sleep(10);
        }
    }
}
