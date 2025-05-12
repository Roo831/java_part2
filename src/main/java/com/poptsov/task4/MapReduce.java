package com.poptsov.task4;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// основной класс с функциями map/reduce
public class MapReduce {
    public static List<KeyValue> map(String fileName, String content) {
        return Arrays.stream(content.split("\\s+"))
                .map(word -> new KeyValue(word, "1"))
                .collect(Collectors.toList());
    }

    public static String reduce(List<String> values) {
        return String.valueOf(values.size());
    }
}