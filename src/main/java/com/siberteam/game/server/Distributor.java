package com.siberteam.game.server;

import java.util.List;
import java.util.Random;

public class Distributor implements Runnable{
    private List<ClientHook> clientsHookList;
    public Distributor(List<ClientHook> clientsHookList){
        this.clientsHookList = clientsHookList;
    }

    @Override
    public void run() {
        while (true) {
            ClientHook randomPlayer = clientsHookList.get(new Random(clientsHookList.size()).nextInt());
        }
    }
}
