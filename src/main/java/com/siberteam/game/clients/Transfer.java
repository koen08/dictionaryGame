package com.siberteam.game.clients;

import java.io.Serializable;

public class Transfer implements Serializable {
    private ClientActions clientActions;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ClientActions getClientActions() {
        return clientActions;
    }

    public void setClientActions(ClientActions clientActions) {
        this.clientActions = clientActions;
    }
}
