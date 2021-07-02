package com.siberteam.game.server;

import java.io.IOException;

public class MainClass {
    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
