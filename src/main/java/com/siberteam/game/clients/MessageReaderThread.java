package com.siberteam.game.clients;

import com.siberteam.game.clients.AnswerServerTransfer;

import java.io.IOException;

public class MessageReaderThread extends Thread {
    private final Client client;

    public MessageReaderThread(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            while (true) {
                AnswerServerTransfer answerServer = client.readerMsgFromServer();
                MainClass.outputMessageConsole(answerServer.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
