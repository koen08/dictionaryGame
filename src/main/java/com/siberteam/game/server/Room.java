package com.siberteam.game.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private List<ClientHook> clientsRoom = new ArrayList<>();

    public Room() {

    }

    public void addClientIntoRoom(ClientHook client) {
        clientsRoom.add(client);
        try {
            messageRoom("Пользователь " + client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteClientFromRoom(ClientHook clientHook) {
        clientsRoom.remove(clientHook);
    }

    public void messageRoom(String msg) throws IOException {
        for (ClientHook clientHook : clientsRoom) {
            clientHook.sendMsgToSender(msg);
        }
    }
}
