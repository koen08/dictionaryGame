package com.siberteam.client;

import java.io.IOException;

public class MessageReaderThread extends Thread {
    private final ClientManager client;
    private boolean isFinishGame = false;
    private boolean roundWaitForWord = false;

    public MessageReaderThread(ClientManager client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            while (true) {
                AnswerServerTransfer answerServer = client.readerMsgFromServer();
                if (answerServer.getMessage().equals("/auto")) {
                    MainClassClient.makeMove();
                    continue;
                }
                if (answerServer.getDictionaryWords() != null) {
                    new FileStreamWorker().writeDequeWordIntoFile(answerServer.getDictionaryWords());
                }
                MainClassClient.outputMessageConsole(answerServer.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isFinishGame() {
        return isFinishGame;
    }

    public void setFinishGame(boolean finishGame) {
        isFinishGame = finishGame;
    }

    public boolean isRoundWaitForWord() {
        return roundWaitForWord;
    }

    public void setRoundWaitForWord(boolean roundWaitForWord) {
        this.roundWaitForWord = roundWaitForWord;
    }
}
