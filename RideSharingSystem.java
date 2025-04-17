package ridesharing;

import java.util.concurrent.*;
import java.util.*;

public class RideSharingSystem {
    private static final int NUM_WORKERS = 5;
    private static final int NUM_TASKS = 20;

    // Shared resources
    private final BlockingQueue<String> taskQueue = new LinkedBlockingQueue<>();
    private final List<String> resultList = Collections.synchronizedList(new ArrayList<>());
    private final ExecutorService executor = Executors.newFixedThreadPool(NUM_WORKERS);

    public static void main(String[] args) {
        RideSharingSystem system = new RideSharingSystem();
        system.start();
    }

    public void start() {
        log("Starting Ride Sharing System...");

        try {
            // Add tasks to the shared queue
            for (int i = 1; i <= NUM_TASKS; i++) {
                String task = "Ride Request #" + i;
                taskQueue.put(task); // This will block if the queue is full
            }

            // Start worker threads
            for (int i = 0; i < NUM_WORKERS; i++) {
                executor.execute(new Worker(i + 1, taskQueue, resultList));
            }

        } catch (Exception e) {
            logError("Error while initializing tasks: " + e.getMessage());
        } finally {
            // Shutdown after all tasks are done
            executor.shutdown();
            try {
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                    log("Forcing shutdown...");
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                logError("Shutdown interrupted: " + e.getMessage());
                executor.shutdownNow();
            }
            log("All threads have completed.");
            log("Final Results: ");
            for(String s: resultList) {
            	System.out.println(s);
            }
        }
    }

    private static void log(String message) {
        System.out.println("[INFO] " + message);
    }

    private static void logError(String message) {
        System.err.println("[ERROR] " + message);
    }

    // Inner Worker Class
    static class Worker implements Runnable {
        private final int workerId;
        private final BlockingQueue<String> taskQueue;
        private final List<String> resultList;

        public Worker(int id, BlockingQueue<String> queue, List<String> results) {
            this.workerId = id;
            this.taskQueue = queue;
            this.resultList = results;
        }

        @Override
        public void run() {
            log("Worker " + workerId + " started.");
            while (true) {
                try {
                    String task = taskQueue.poll(2, TimeUnit.SECONDS);
                    if (task == null) {
                        break; // Exit if no task is available within timeout
                    }

                    log("Worker " + workerId + " processing: " + task);
                    Thread.sleep(1000); // Simulate processing delay
                    resultList.add("Worker " + workerId + " completed: " + task);

                } catch (InterruptedException e) {
                    logError("Worker " + workerId + " interrupted.");
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    logError("Worker " + workerId + " encountered error: " + e.getMessage());
                }
            }
            log("Worker " + workerId + " finished.");
        }

        private void log(String message) {
            System.out.println("[Worker " + workerId + "] " + message);
        }

        private void logError(String message) {
            System.err.println("[Worker " + workerId + "] ERROR: " + message);
        }
    }
}
