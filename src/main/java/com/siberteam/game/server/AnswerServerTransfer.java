package com.siberteam.game.server;

import java.io.Serializable;

public class AnswerServerTransfer implements Serializable {
    private String message;

    public AnswerServerTransfer(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
