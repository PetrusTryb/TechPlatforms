package com.trybisz;

import java.util.concurrent.LinkedBlockingQueue;

public class TaskQueue {
    private final LinkedBlockingQueue<Task> queue = new LinkedBlockingQueue<>();
    private int taskCounter = 0;

    public synchronized void addTask(int N) {
        queue.add(new Task(taskCounter++, N));
        notify();
    }

    public synchronized Task getTask() throws InterruptedException {
        if (queue.isEmpty()) wait();
        return queue.poll();
    }

}
