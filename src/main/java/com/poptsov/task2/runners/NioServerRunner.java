package com.poptsov.task2.runners;



import com.poptsov.task2.IEchoServer;
import com.poptsov.task2.NIOrealisation.NioEchoServer;

public class NioServerRunner {
    public static void main(String[] args) {
        IEchoServer server = new NioEchoServer();
        server.server();
    }
}