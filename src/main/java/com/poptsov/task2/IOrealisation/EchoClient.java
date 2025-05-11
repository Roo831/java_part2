package com.poptsov.task2.IOrealisation;

import com.poptsov.task2.EchoServerProperties;
import com.poptsov.task2.IEchoClient;

import java.io.*;
import java.net.*;

public class EchoClient implements IEchoClient {
    private final String HOST = EchoServerProperties.ADDRESS;
    private final int PORT = EchoServerProperties.PORT;

    public void client() {
        try (
                Socket socket = new Socket(HOST, PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Подключено к серверу. Введите сообщение:");

            String userInput;
            while ((userInput = consoleInput.readLine()) != null) {
                out.println(userInput); // Отправляем серверу
                System.out.println(in.readLine()); // Читаем ответ
            }

        }
        catch (SocketException e) {
            System.out.println(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}