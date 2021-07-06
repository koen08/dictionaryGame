package com.siberteam.client;

import java.io.IOException;

public class MessageReaderThread extends Thread {
    private final ClientManager client;

    public MessageReaderThread(ClientManager client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            while (true) {
                AnswerServerTransfer answerServer = client.readerMsgFromServer();
                if (answerServer.getDictionaryWords() != null) {
                    new FileStreamWorker().writeDequeWordIntoFile(answerServer.getDictionaryWords());
                }
                MainClassClient.outputMessageConsole(answerServer.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
