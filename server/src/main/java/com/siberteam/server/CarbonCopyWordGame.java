package com.siberteam.server;

import java.io.IOException;
import java.util.*;

public class CarbonCopyWordGame implements Game {
    private Room room;
    private Random random;
    private List<Set<String>> resultsCopyWords;

    public CarbonCopyWordGame(Room room) {
        this.room = room;
        resultsCopyWords = new ArrayList<>();
    }

    @Override
    public void startGame() throws IOException {
        doSlotsForClient();
        int round = 0;
        random = new Random();
        room.messageRoom(Color.ANSI_GREEN.paint("Ожидание хода всех игроков"));
        while (!isOneWinner()) {
            if (everyoneMadeMove()) {
                round++;
                room.messageRoom(Color.ANSI_YELLOW.paint("Раунд №" + round));
                for (int i = 0; i < room.getClientsRoom().size(); i++) {
                    resultsCopyWords.get(i).add(
                            room.getSlotClients().get(getRandomIndexWithRepeat(i)).toString());
                }
                finishRound();
                room.messageRoom(Color.ANSI_GREEN.paint("Ожидание хода всех игроков"));
            }
        }
        Client clientWinner = searchWinner();
        room.messageRoom(Color.ANSI_YELLOW.paint("Игрок " + clientWinner.getNickName() + " победил!"));
    }

    private void finishRound() {
        for (int i = 0; i < room.getSlotClients().size(); i++) {
            room.getSlotClients().get(i).setLength(0);
        }
    }

    private void doSlotsForClient() {
        for (int i = 0; i < room.getClientsRoom().size(); i++) {
            resultsCopyWords.add(new HashSet<>());
        }
    }

    private boolean everyoneMadeMove() {
        for (StringBuilder word : room.getSlotClients()) {
            if (word.toString().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isOneWinner() {
        int commonLength = sumLengthAllClient();
        for (Set<String> resultsCopyWord : resultsCopyWords) {
            if ((commonLength - resultsCopyWord.size()) == 0) {
                return true;
            }
        }
        return false;
    }

    private int sumLengthAllClient() {
        int commonLength = 0;
        for (int i = 0; i < room.getDictionaryClients().size(); i++) {
            commonLength += room.getDictionaryClients().get(i).size();
        }
        return commonLength;
    }

    @Override
    public Client searchWinner() {
        Client client = null;
        int commonLength = sumLengthAllClient();
        for (int i = 0; i < resultsCopyWords.size(); i++) {
            if ((commonLength - resultsCopyWords.get(i).size()) == 0) {
                client = room.getClientsRoom().get(i);
            }
        }
        return client;
    }

    @Override
    public int getRandomIndexWithRepeat(int player) {
        int playerLostWordRandom = 0;
        do {
            playerLostWordRandom = random.nextInt(room.getClientsRoom().size());
        } while (player == playerLostWordRandom);
        return playerLostWordRandom;
    }
}
