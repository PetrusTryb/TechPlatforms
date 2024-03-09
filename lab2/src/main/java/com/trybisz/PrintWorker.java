package com.trybisz;

public class PrintWorker implements Runnable{
    private final ResultsQueue resultsQueue;

    public PrintWorker(ResultsQueue resultsQueue) {
        this.resultsQueue = resultsQueue;
    }
    @Override
    public void run() {
        while(true){
            try {
                TaskResult result = resultsQueue.getResult();
                System.out.println("Task #" + result.id() + " has returned result: " + result.result());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
