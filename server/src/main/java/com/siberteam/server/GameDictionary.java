package com.siberteam.server;

import java.io.IOException;
import java.util.Deque;
import java.util.Random;

public class GameDictionary {
    private final Room room;

    public GameDictionary(Room room) {
        this.room = room;
    }

    public void startGame() throws IOException, InterruptedException {
        room.messageRoom(Color.ANSI_YELLOW.paint("Игра началась!"));
        while (!isOneWinner()) {
            int playerMakeMoveRandom = new Random().nextInt(room.getClientsRoom().size());
            int playerLostWordRandom = getRandomIndexWithRepeat(playerMakeMoveRandom);
            if (room.getDictionaryClients().get(playerLostWordRandom).isEmpty()
                    || room.getDictionaryClients().get(playerMakeMoveRandom).isEmpty()) {
                continue;
            }
            room.getDictionaryClients().get(playerMakeMoveRandom).add(
                    room.getDictionaryClients().get(playerLostWordRandom).pop());
            room.getClientsRoom().get(playerLostWordRandom).sendMsgToSender(Color.ANSI_BLUE.paint(
                    "У вас осталось " + room.getDictionaryClients().get(playerLostWordRandom).size() + " слов"));
            Thread.sleep(250);
        }
        Client winnerClient = searchWinner();
        if (winnerClient != null) {
            room.messageRoom(Color.ANSI_YELLOW.paint("Игрок " + winnerClient.getNickName() + " выигрывает игру!"));
            winnerClient.sendMsgWithDequeToSender(
                    "Вы загрузили слова игроков", room.getDictionaryClients().get(
                            room.getClientsRoom().indexOf(winnerClient)));
        }
    }

    private boolean isOneWinner() {
        int countLosers = 0;
        for (Deque<String> dictionaryClient : room.getDictionaryClients()) {
            if (dictionaryClient.isEmpty()) {
                countLosers++;
            }
        }
        return countLosers + 1 == room.getClientsRoom().size();
    }

    private Client searchWinner() {
        Client client = null;
        for (int i = 0; i < room.getDictionaryClients().size(); i++) {
            if (!room.getDictionaryClients().get(i).isEmpty()) {
                client = room.getClientsRoom().get(i);
            }
        }
        return client;
    }

    private int getRandomIndexWithRepeat(int player) {
        int playerLostWordRandom = 0;
        do {
            playerLostWordRandom = new Random().nextInt(room.getClientsRoom().size());
        } while (player == playerLostWordRandom);
        return playerLostWordRandom;
    }
}
