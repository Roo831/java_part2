package com.poptsov.task4;

import java.util.*;
import java.util.concurrent.*;

public class Coordinator {
    private final Queue<MapTask> mapTasks = new ConcurrentLinkedQueue<>();
    private final Queue<ReduceTask> reduceTasks = new ConcurrentLinkedQueue<>();
    private final Set<Integer> completedMapTasks = ConcurrentHashMap.newKeySet();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final int numReduceTasks;

    public Coordinator(List<String> inputFiles, int numReduceTasks) {
        this.numReduceTasks = numReduceTasks;
        // создаем map задачи для каждого файла
        for (int i = 0; i < inputFiles.size(); i++) {
            mapTasks.add(new MapTask(i, inputFiles.get(i), numReduceTasks));
        }
    }

    public synchronized Task getTask() {
        if (!mapTasks.isEmpty()) {
            return mapTasks.poll();
        } else if (completedMapTasks.size() == mapTasks.size() + reduceTasks.size() && !reduceTasks.isEmpty()) {
            return reduceTasks.poll();
        }
        return null; // все задачи выполнены
    }

    public void startWorkers(int numWorkers) {
        for (int i = 0; i < numWorkers; i++) {
            executor.submit(new Worker(this));
        }
    }

    public synchronized void completeMapTask(int taskId) {
        completedMapTasks.add(taskId);
        // когда все map задачи выполнены, создаем reduce задачи
        if (completedMapTasks.size() == mapTasks.size()) {
            for (int i = 0; i < numReduceTasks; i++) {
                reduceTasks.add(new ReduceTask(i, numReduceTasks));
            }
        }
    }
}