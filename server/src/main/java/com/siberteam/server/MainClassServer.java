package com.siberteam.server;

import java.io.IOException;

public class MainClassServer {
    public static void main(String[] args) {
        try {
            System.out.println("Сервер начал свою работу...");
            Server server = new Server();
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
