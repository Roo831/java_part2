package com.poptsov.task4;

import java.util.List;

public class Task4Runner {
    public static void main(String[] args) {
        List<String> inputFiles = List.of("file1.txt", "file2.txt");
        int numReduceTasks = 3;
        int numWorkers = 4;

        Coordinator coordinator = new Coordinator(inputFiles, numReduceTasks);
        coordinator.startWorkers(numWorkers);
    }
}
