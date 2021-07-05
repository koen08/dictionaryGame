package com.siberteam.game.clients;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

public class Transfer implements Serializable {
    private ClientActions clientActions;
    private String message;
    private Deque<String> dictionaryWords = new ArrayDeque<>();

    public Transfer(ClientActions clientActions, String message) {
        this.clientActions = clientActions;
        this.message = message;
    }

    public Deque<String> getDictionaryWords() {
        return dictionaryWords;
    }

    public void setDictionaryWords(Deque<String> dictionaryWords) {
        this.dictionaryWords = dictionaryWords;
    }

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
