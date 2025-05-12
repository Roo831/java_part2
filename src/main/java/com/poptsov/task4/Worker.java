package com.poptsov.task4;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Worker implements Runnable {
    private final Coordinator coordinator;

    public Worker(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void run() {
        while (true) {
            Task task = coordinator.getTask();
            if (task == null) break;

            if (task instanceof MapTask) {
                processMapTask((MapTask) task);
            } else {
                processReduceTask((ReduceTask) task);
            }
        }
    }

    private void processMapTask(MapTask task) {
        try {
            String content = readFile(task.getFileName()); // читаем все данные из файла в строку
            List<KeyValue> kvs = MapReduce.map(task.getFileName(), content); // разбиваем эту строку сначала на слова, затем маппим в объект, где ключ - слово, а значение - 1.

            // группируем по reduce-бакетам
            Map<Integer, List<KeyValue>> buckets = new HashMap<>();
            for (KeyValue kv : kvs) {
                int bucket = Math.abs(kv.getKey().hashCode()) % task.getNumReduceTasks(); // создаем по бакету на каждую reduce задачу
                buckets.computeIfAbsent(bucket, k -> new ArrayList<>()).add(kv); // маппим ид бакета как ключ, а данные которые в будущем проредуцируем как list<KeyValue>.
            }

            // записываем в промежуточные файлы
            for (Map.Entry<Integer, List<KeyValue>> entry : buckets.entrySet()) { // для каждого
                writeToFile(entry.getValue(), "mr-" + task.getTaskId() + "-" + entry.getKey());
            }

            coordinator.completeMapTask(task.getTaskId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processReduceTask(ReduceTask task) {
        try {
            // читаем все промежуточные файлы для этого reduce-бакета
            List<KeyValue> kvs = new ArrayList<>();
            for (int i = 0; i < task.getNumMapTasks(); i++) {
                String filename = "mr-" + i + "-" + task.getTaskId();
                kvs.addAll(readKeyValues(filename));
            }

            // сортируем по ключам
            kvs.sort(Comparator.comparing(KeyValue::getKey));

            // группируем по ключам и применяем reduce
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("out-" + task.getTaskId()))) {
                String currentKey = null;
                List<String> values = new ArrayList<>();

                for (KeyValue kv : kvs) { // значения KeyValue складываются вместе для тех KeyValue, у которых ключи одинаковы. Затем этот ключ и количество его повторений записываются в файл.
                    if (!kv.getKey().equals(currentKey)) {
                        if (currentKey != null) {
                            String result = MapReduce.reduce(values);
                            writer.write(currentKey + " " + result + "\n");
                        }
                        currentKey = kv.getKey();
                        values.clear();
                    }
                    values.add(kv.getValue());
                }

                if (!values.isEmpty()) {
                    String result = MapReduce.reduce(values);
                    writer.write(currentKey + " " + result + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFile(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }

    private void writeToFile(List<KeyValue> kvs, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (KeyValue kv : kvs) {
                writer.write(kv.getKey() + " " + kv.getValue() + "\n");
            }
        }
    }

    private List<KeyValue> readKeyValues(String filename) throws IOException {
        List<KeyValue> kvs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                kvs.add(new KeyValue(parts[0], parts[1]));
            }
        }
        return kvs;
    }
}