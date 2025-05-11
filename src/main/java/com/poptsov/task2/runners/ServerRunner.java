package com.poptsov.task2.runners;

import com.poptsov.task2.IEchoServer;
import com.poptsov.task2.IOrealisation.EchoServer;

public class ServerRunner {
    public static void main(String[] args) {
        IEchoServer serverIO = new EchoServer();
        serverIO.server();
    }
}
