package com.siberteam.server;

import java.io.IOException;
import java.util.Deque;
import java.util.Random;

public class ThiefWordGame implements Game {
    private final Room room;

    public ThiefWordGame(Room room) {
        this.room = room;
    }

    public void startGame(TypePlay typePlay) throws IOException, InterruptedException {
        room.messageRoom(Color.ANSI_YELLOW.paint("Игра началась!"));
        Random random = new Random();
        while (!isOneWinner()) {
            int playerMakeMoveRandom = random.nextInt(room.getClientsRoom().size());
            int playerLostWordRandom = getRandomIndexWithRepeat(playerMakeMoveRandom);
            if (room.getDictionaryClients().get(playerLostWordRandom).isEmpty()
                    || room.getDictionaryClients().get(playerMakeMoveRandom).isEmpty()) {
                continue;
            }
            room.getDictionaryClients().get(playerMakeMoveRandom).add(
                    room.getDictionaryClients().get(playerLostWordRandom).pop());
            room.getClientsRoom().get(playerLostWordRandom).sendMsgToClient(Color.ANSI_BLUE.paint(
                    "У вас осталось " + room.getDictionaryClients().get(playerLostWordRandom).size() + " слов"));
            Thread.sleep(100);
        }
        Client winnerClient = searchWinner();
        if (winnerClient != null) {
            room.messageRoom(Color.ANSI_YELLOW.paint("Игрок " + winnerClient.getNickName() + " выигрывает игру!"));
            winnerClient.sendMsgWithCollectionToSender(
                    "Вы загрузили слова игроков", room.getDictionaryClients().get(
                            room.getClientsRoom().indexOf(winnerClient)));
        }
    }

    @Override
    public boolean isOneWinner() {
        int countLosers = 0;
        for (Deque<String> dictionaryClient : room.getDictionaryClients()) {
            if (dictionaryClient.isEmpty()) {
                countLosers++;
            }
        }
        return countLosers + 1 == room.getClientsRoom().size();
    }

    @Override
    public Client searchWinner() {
        Client client = null;
        for (int i = 0; i < room.getDictionaryClients().size(); i++) {
            if (!room.getDictionaryClients().get(i).isEmpty()) {
                client = room.getClientsRoom().get(i);
            }
        }
        return client;
    }

    @Override
    public int getRandomIndexWithRepeat(int player) {
        int playerLostWordRandom = 0;
        Random random = new Random();
        do {
            playerLostWordRandom = random.nextInt(room.getClientsRoom().size());
        } while (player == playerLostWordRandom);
        return playerLostWordRandom;
    }
}
