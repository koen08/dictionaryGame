package com.siberteam.server;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class CarbonCopyWordGame implements Game {
    private final Room room;
    private Random random;
    private final List<Set<String>> resultsCopyWords;

    public CarbonCopyWordGame(Room room) {
        this.room = room;
        resultsCopyWords = new CopyOnWriteArrayList<>();
    }

    @Override
    public void startGame(TypePlay typePlay) throws IOException {
        doSlotsForClient();
        int round = 0;
        random = new Random();
        room.messageRoom(
                Color.ANSI_GREEN.paint("Ожидание хода всех игроков. Введите команду /send чтобы сделать ход"));
        if (typePlay.equals(TypePlay.AUTO_PLAY)) {
            room.messageRoom("/auto");
        }
        while (!isOneWinner()) {
            if (everyoneMadeMove()) {
                round++;
                room.messageRoom(Color.ANSI_YELLOW.paint("Раунд №" + round));
                for (int i = 0; i < room.getClientsRoom().size(); i++) {
                    resultsCopyWords.get(i).add(
                            room.getSlotClients().get(getRandomIndexWithRepeat(i)).toString());
                }
                finishRound();
                sendActualDictionaryAllClients();
                room.messageRoom(
                        Color.ANSI_GREEN.paint(
                                "Ожидание хода всех игроков. Введите команду /send чтобы сделать ход"));

                if (typePlay.equals(TypePlay.AUTO_PLAY)) {
                    room.messageRoom("/auto");
                }
            }
        }
        Client clientWinner = searchWinner();
        room.messageRoom(Color.ANSI_YELLOW.paint("Игрок " + clientWinner.getNickName() + " победил!"));
        clientWinner.sendMsgWithCollectionToSender(
                "Вы загрузили слова игроков", resultsCopyWords.get(
                        room.getClientsRoom().indexOf(clientWinner)));
    }

    private void sendActualDictionaryAllClients() throws IOException {
        for (int i = 0; i < room.getClientsRoom().size(); i++) {
            room.getClientsRoom().get(i).sendMsgToClient(
                    Color.ANSI_BLUE.paint(
                            "На данный момент вы скопировали следующие слова: " + resultsCopyWords.get(i).toString()));
        }
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
        for (int i = 0; i < resultsCopyWords.size(); i++) {
            if ((commonLength - (resultsCopyWords.get(i).size() + room.getDictionaryClients().get(i).size())) == 0) {
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
            if ((commonLength - (resultsCopyWords.get(i).size() + room.getDictionaryClients().get(i).size())) == 0) {
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
