package com.trybisz;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskQueue taskQueue = new TaskQueue();
        ResultsQueue resultsQueue = new ResultsQueue();
        int threads = args.length > 0 ? Integer.parseInt(args[0]) : Runtime.getRuntime().availableProcessors();
        Thread[] primeFinderWorker = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            primeFinderWorker[i] = new Thread(new PrimeWorker(taskQueue, resultsQueue));
            primeFinderWorker[i].start();
        }
        Thread printWorker = new Thread(new PrintWorker(resultsQueue));
        printWorker.start();
        String command;
        Scanner scanner = new Scanner(System.in);
        while(true){
            command = scanner.nextLine();
            int index;
            try{
                index = Integer.parseInt(command);
            }
            catch(NumberFormatException e){
                if (command.equals("exit")) {
                    for (int i = 0; i < threads; i++) {
                        primeFinderWorker[i].interrupt();
                    }
                    printWorker.interrupt();
                    break;
                }
                else {
                    System.out.println("Invalid command. Enter index of prime number to find or 'exit' to quit.");
                    continue;
                }
            }
            taskQueue.addTask(index);
        }
    }
}