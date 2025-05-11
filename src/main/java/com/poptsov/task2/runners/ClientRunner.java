package com.poptsov.task2.runners;

import com.poptsov.task2.IEchoClient;
import com.poptsov.task2.IOrealisation.EchoClient;

import java.util.Scanner;

public class ClientRunner {
    public static void main(String[] args) {
        IEchoClient client = new EchoClient();
        client.client();
    }
}