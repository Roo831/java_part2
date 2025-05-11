package com.poptsov.task2.NIOrealisation;

import com.poptsov.task2.IEchoClient;
import com.poptsov.task2.EchoServerProperties;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NioEchoClient implements IEchoClient {

    private final String HOST = EchoServerProperties.ADDRESS;
    private final int PORT = EchoServerProperties.PORT;

    public void client() {
        try {
            SocketChannel client = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            System.out.println("Подключено к NIO серверу. Введите сообщение:");

            Scanner scanner = new Scanner(System.in);
            ByteBuffer buffer = ByteBuffer.allocate(256);

            while (true) {
                String message = scanner.nextLine();
                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }

                // Отправка сообщения
                buffer.put(message.getBytes());
                buffer.flip();
                client.write(buffer);
                buffer.clear();

                // Получение ответа
                client.read(buffer);
                buffer.flip();
                String response = new String(buffer.array()).trim();
                System.out.println(response);
                buffer.clear();
            }

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}