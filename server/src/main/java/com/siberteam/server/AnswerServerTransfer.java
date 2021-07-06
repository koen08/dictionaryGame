package com.siberteam.server;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

public class AnswerServerTransfer implements Serializable {
    private String message;
    private Deque<String> dictionaryWords = new ArrayDeque<>();

    public AnswerServerTransfer(String message, Deque<String> deque) {
        this.message = message;
        this.dictionaryWords = deque;
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
}
