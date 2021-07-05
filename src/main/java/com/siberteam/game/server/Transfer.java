package com.siberteam.game.server;

import com.siberteam.game.clients.ClientActions;

import java.io.Serializable;
import java.util.Deque;

public class Transfer implements Serializable {
    private ClientActions clientActions;
    private String message;
    private Deque<String> dictionaryWords;

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

    public Deque<String> getDictionaryWords() {
        return dictionaryWords;
    }

    public void setDictionaryWords(Deque<String> dictionaryWords) {
        this.dictionaryWords = dictionaryWords;
    }
}
