package com.siberteam.game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server {
    public static final int PORT = 8000;
    private List<ClientHook> clientsHookList;
    private List<Room> rooms;

    public void startServer() throws IOException {
        clientsHookList = new LinkedList<>();
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            Socket socket;
            while ((socket = serverSocket.accept()) != null){
                clientsHookList.add(new ClientHook(socket));
            }
        }
    }

}
