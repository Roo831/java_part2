package com.poptsov.task2.runners;

import com.poptsov.task2.IEchoClient;
import com.poptsov.task2.NIOrealisation.NioEchoClient;

public class NioClientRunner {
    public static void main(String[] args) {
        IEchoClient client = new NioEchoClient();
        client.client();
    }
}