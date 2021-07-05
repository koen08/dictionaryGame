package com.siberteam.game.server;

import java.io.IOException;
import java.util.Deque;
import java.util.Random;

public class GameDictionary {
    private final Room room;

    public GameDictionary(Room room) {
        this.room = room;
    }

    public void startGame() throws IOException {
        while (!isOneWinner()) {
            int playerMakeMoveRandom = new Random().nextInt(room.getClientsRoom().size());
            int playerLostWordRandom = getRandomIndexWithRepeat(playerMakeMoveRandom);
            room.getDictionaryClients().get(playerMakeMoveRandom).add(
                    room.getDictionaryClients().get(playerLostWordRandom).pop());
//            room.messageRoom(room.getClientsRoom().get(playerMakeMoveRandom).getNickName() + " забирает слово у " +
//                    room.getClientsRoom().get(playerLostWordRandom).getNickName());
            room.getClientsRoom().get(playerLostWordRandom).sendMsgToSender(Color.ANSI_BLUE.paint(
                    "У вас осталось " + room.getDictionaryClients().get(playerLostWordRandom).size() + " слов"));
        }
        Client winnerClient = searchWinner();
        if (winnerClient != null) {
            room.messageRoom(Color.ANSI_YELLOW.paint("Игрок " + winnerClient.getNickName() + " выигрывает игру!"));
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

    private Client searchWinner(){
        Client client = null;
        for (int i = 0; i < room.getDictionaryClients().size(); i++){
            if (!room.getDictionaryClients().get(i).isEmpty()){
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
