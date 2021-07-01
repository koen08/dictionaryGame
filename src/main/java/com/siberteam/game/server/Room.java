package com.siberteam.game.server;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private List<ClientHook> clientsRoom = new ArrayList<>();

    public Room() {

    }

    public void addClientIntoRoom(ClientHook client){
        clientsRoom.add(client);
        messageRoom("Пользователь " + client.);
    }

    public void deleteClientFromRoom(ClientHook clientHook){
        clientsRoom.remove(clientHook);
    }

    public void messageRoom(String msg){

    }
}
