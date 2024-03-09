package com.trybisz;

import java.util.concurrent.LinkedBlockingQueue;

public class ResultsQueue {
    private final LinkedBlockingQueue<TaskResult> queue = new LinkedBlockingQueue<>();

    public synchronized void addResult(TaskResult result) {
        queue.add(result);
        notify();
    }

    public synchronized TaskResult getResult() throws InterruptedException {
        if (queue.isEmpty()) wait();
        return queue.poll();
    }
}
