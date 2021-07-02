package com.siberteam.game.server;

import com.siberteam.game.clients.ClientActions;

import java.io.Serializable;

public class Transfer implements Serializable {
    private ClientActions clientActions;
    private String message;

    public ClientActions getClientActions() {
        return clientActions;
    }

    public void setClientActions(ClientActions clientActions) {
        this.clientActions = clientActions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
