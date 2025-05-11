package com.poptsov.task2.NIOrealisation;
import com.poptsov.task2.IEchoServer;
import com.poptsov.task2.EchoServerProperties;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioEchoServer implements IEchoServer {

    private final int PORT = EchoServerProperties.PORT;

    public void server() {
        try {
            // Создаем Selector и ServerSocketChannel
            Selector selector = Selector.open(); // ожидает системных вызовов, использует нативные системные решения для взаимодействия с внешними соединениями
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(PORT));
            serverSocket.configureBlocking(false); //
            serverSocket.register(selector, SelectionKey.OP_ACCEPT); // устанавливаем селектор в сокет и настраиваем его на прием соединений.

            System.out.println("NIO Сервер запущен на порту " + PORT);

            while (true) {
                selector.select(); // Блокируется до появления событий
                Set<SelectionKey> selectedKeys = selector.selectedKeys(); // получаем события в сет
                Iterator<SelectionKey> iter = selectedKeys.iterator(); // получаем итератор, так как это fail fast коллекция.

                while (iter.hasNext()) { // фактически как только приходит подключение - мы сразу же обрабатываем его,а если пришло много подключений за раз - он их сразу обработает или они в сете будут лежать и ждать обработки?
                    SelectionKey key = iter.next(); // получаем событие(соединение)

                    if (key.isAcceptable()) { // если еще не зарегистрирован
                        register(selector, serverSocket); // регистрируем
                    }

                    if (key.isReadable()) { // если есть то, что нужно обработать
                        processClientRequest(key); // обрабатываем
                    }
                    iter.remove(); // удаляем по завершении
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        System.out.println("Новый клиент подключен: " + client.getRemoteAddress());
    }

    private void processClientRequest(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel(); // получаем из ключа наш канал клиента
        ByteBuffer buffer = ByteBuffer.allocate(256); // создаем буфер на 256 бйат

        try {
            int read = client.read(buffer); // читаем данные из клиент канала в буфер, записываем количество прочитанных байт в переменную.
            if (read == -1) { // если буфер пустой
                System.out.println("Клиент отключился: " + client.getRemoteAddress());
                client.close(); // закрываем сокет и выходим из метода обработки
                return;
            }

            buffer.flip(); // готовим буфер к чтению
            String message = new String(buffer.array()).trim(); // читаем из буфера сообщение
            System.out.println("[NIO Сервер] Получено: " + message); // Выводим в логах сервера сообщение, отправленное клиентом

            // Отправляем эхо-ответ
            String response = "Эхо: " + message + ". NIO поток: " + Thread.currentThread().threadId();
            client.write(ByteBuffer.wrap(response.getBytes())); // пишем в этот же клиент используя буфер сообщение, запакованное в байт массив
            buffer.clear(); // чистим буфер
        } catch (IOException e) {
            System.out.println("Клиент аварийно отключился: " + client.getRemoteAddress());
            client.close(); // в случае ошибки закрываем подключение
        }
    }
}