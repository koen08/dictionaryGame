package com.siberteam.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    public static final int PORT = 8000;
    private List<Client> clientsHookList;
    private Map<Integer, Room> rooms;

    public void startServer() throws IOException {
        rooms = new ConcurrentHashMap<>();
        clientsHookList = new CopyOnWriteArrayList<>();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                clientsHookList.add(new Client(socket, this));
                System.out.println(Color.ANSI_YELLOW.paint("Количество пользователей в системе " +
                        clientsHookList.size()));
            }
        }
    }

    public String createRoom(Room room) {
        int randomId = new Random().nextInt(1000);
        rooms.putIfAbsent(randomId, room);
        return "ID комнаты для подключения - " + randomId;
    }

    public void removeClient(Client client) {
        clientsHookList.remove(client);
    }

    public Room searchRoom(Integer idRoom) {
        return rooms.getOrDefault(idRoom, null);
    }
}
