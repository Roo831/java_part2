package com.poptsov.task3;


/**
 * Потокобезопасная реализация RingBuffer с использованием synchronized
 */
public class SynchronizedRingBuffer<T> implements IRingBuffer<T> {

    private final Object[] buffer; // целевой массив элементов
    private int head = 0; // голова, меняется, когда мы пишем в буффер
    private int tail = 0; // хвост, меняется, когда мы читаем из буффера
    private int count = 0; // текущее количество элементов
    private final int capacity; // общая вместимость

    public SynchronizedRingBuffer(int capacity) {
        this.buffer = new Object[capacity];
        this.capacity = capacity;
    }

    @Override
    public synchronized void put(T item) {
        buffer[head] = item;
        head = (head + 1) % capacity; // передвинули вперед указатель записи.

        if (count < capacity) {
            count++;
        } else {
            // буфер полон, перезаписываем старый элемент
            tail = (tail + 1) % capacity;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized T get() {
        if (isEmpty()) {
            return null;
        }
        T item = (T) buffer[tail];
        buffer[tail] = null;

        tail = (tail + 1) % capacity; // передвинули вперед указатель чтения
        count--;
        return item;
    }

    @Override
    public synchronized boolean isEmpty() {
        return count == 0;
    }

    @Override
    public synchronized boolean isFull() {
        return count == capacity;
    }

    @Override
    public synchronized int size() {
        return count;
    }
}