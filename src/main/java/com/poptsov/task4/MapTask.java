package com.poptsov.task4;

class MapTask implements Task {
    private final int taskId;
    private final String fileName;
    private final int numReduceTasks;

    public MapTask(int taskId, String fileName, int numReduceTasks) {
        this.taskId = taskId;
        this.fileName = fileName;
        this.numReduceTasks = numReduceTasks;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getFileName() {
        return fileName;
    }

    public int getNumReduceTasks() {
        return numReduceTasks;
    }
}