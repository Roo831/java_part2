package com.poptsov.task4;

class ReduceTask implements Task {
    private final int taskId;
    private final int numMapTasks;

    public ReduceTask(int taskId, int numMapTasks) {
        this.taskId = taskId;
        this.numMapTasks = numMapTasks;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getNumMapTasks() {
        return numMapTasks;
    }


}