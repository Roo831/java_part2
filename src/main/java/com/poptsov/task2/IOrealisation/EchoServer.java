package com.poptsov.task2.IOrealisation;
import com.poptsov.task2.EchoServerProperties;
import com.poptsov.task2.IEchoServer;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer implements IEchoServer {
    private final int PORT = EchoServerProperties.PORT;
    private final int THREAD_POOL_SIZE = 2;

    public void server() {
        try (ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
             ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Ждём подключения
                System.out.println("Новый клиент: " + clientSocket.getInetAddress());

               threadPool.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
