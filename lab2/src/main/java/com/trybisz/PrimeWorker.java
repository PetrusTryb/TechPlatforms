package com.trybisz;

public class PrimeWorker implements Runnable {
    private final TaskQueue taskQueue;
    private final ResultsQueue resultsQueue;

    public PrimeWorker(TaskQueue taskQueue, ResultsQueue resultsQueue) {
        this.taskQueue = taskQueue;
        this.resultsQueue = resultsQueue;
    }
    @Override
    public void run() {
        while (true) {
            try {
                Task task = taskQueue.getTask();
                int n = task.query();
                int prime = 2;
                int count = 0;
                while (count < n) {
                    prime++;
                    boolean isPrime = true;
                    for (int i = 2; i <= Math.sqrt(prime); i++) {
                        if (prime % i == 0) {
                            isPrime = false;
                            break;
                        }
                    }
                    if (isPrime) {
                        count++;
                    }
                    if(Thread.interrupted()){
                        return;
                    }
                }
                resultsQueue.addResult(new TaskResult(task.id(), prime));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
