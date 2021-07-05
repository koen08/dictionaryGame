package com.siberteam.game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    public static final int PORT = 8000;
    private List<Client> clientsHookList;
    private Map<Integer,Room> rooms;

    public void startServer() throws IOException {
        clientsHookList = new LinkedList<>();
        rooms = new HashMap();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                clientsHookList.add(new Client(socket, this));
            }
        }
    }

    public String createRoom(Room room) {
        int randomId = new Random().nextInt(1000);
        if (rooms.get(randomId) == null){
            rooms.put(randomId, room);
        }
        return "ID комнаты для подключения - " + randomId;
    }

    public Room searchRoom(Integer idRoom){
        return rooms.getOrDefault(idRoom, null);
    }
}
