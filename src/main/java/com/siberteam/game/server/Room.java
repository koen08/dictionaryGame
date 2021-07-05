package com.siberteam.game.server;

import java.io.IOException;
import java.util.*;

public class Room {
    private List<Client> clientsRoom = new ArrayList<>();
    private List<Deque<String>> dictionaryClients = new ArrayList<>();
    private boolean isGameStart = false;

    public void startGame(Client client) throws IOException {
        if (client.equals(clientsRoom.get(0))) {
            if (clientsRoom.size() > 1) {
                isGameStart = true;
                GameDictionary gameDictionary = new GameDictionary(this);
                if (isAllReadyWithDictionary() && isUniquenessDictionaries()) {
                    gameDictionary.startGame();
                }
            } else client.sendMsgToSender("Комната должна иметь хотя бы 2 участника");
        } else client.sendMsgToSender("Вы не явялетесь создателем комнаты");
    }

    private boolean isUniquenessDictionaries() throws IOException {
        for (int i = 0; i < clientsRoom.size()-1; i++){
            for (int j = i+1; j < clientsRoom.size(); j++){
                for (String s : dictionaryClients.get(i)) {
                    if (dictionaryClients.get(j).contains(s)) {
                        messageRoom("Слово - " + s + " - встречается у " +
                                clientsRoom.get(i).getNickName() + " и " +
                                clientsRoom.get(j).getNickName());
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void addClientIntoRoom(Client client) {
        try {
            if (!clientsRoom.contains(client)) {
                clientsRoom.add(client);
                dictionaryClients.add(new ArrayDeque<>());
                messageRoom("Пользователь " + client.getNickName() + " подключился к комнате");
                messageRoom("Количество игроков в комнате - " + clientsRoom.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteClientFromRoom(Client client) throws IOException {
        clientsRoom.remove(client);
        client.setRoom(null);
        client.sendMsgToSender("Вы покинули группу");
        messageRoom(client.getNickName() + " покинул комнату" + ", размер комнаты - " + clientsRoom.size());
    }

    public void deleteRoom(Client client) throws IOException {
        if (client.equals(clientsRoom.get(0))) {
            for (Client cl : clientsRoom) {
                cl.sendMsgToSender("Комната была удалена, вы вернулись в главное меню");
                cl.setRoom(null);
            }
        } else client.sendMsgToSender("Вы не явялетесь создателем комнаты");
    }

    public void downloadDictionary(Client client, Deque<String> deque) throws IOException {
        int indexClientsIntoRoom = clientsRoom.indexOf(client);
        deleteAllWords(indexClientsIntoRoom);
        while (!deque.isEmpty()) {
            dictionaryClients.get(indexClientsIntoRoom).add(deque.pop());
        }
        client.sendMsgToSender(Color.ANSI_GREEN.paint("Вы успешно загрузили свой словарь"));
    }

    private void deleteAllWords(int index){
        while (!dictionaryClients.get(index).isEmpty()){
            dictionaryClients.get(index).pop();
        }
    }

    public boolean isAllReadyWithDictionary() throws IOException {
        for (Client client : clientsRoom) {
            int indexClientsIntoRoom = clientsRoom.indexOf(client);
            if (dictionaryClients.get(indexClientsIntoRoom).isEmpty()) {
                messageRoom( Color.ANSI_RED.paint("Игрок " + client.getNickName() + " не загрузил словарь"));
                return false;
            }
        }
        return true;
    }

    public void messageRoom(String msg) throws IOException {
        for (Client client : clientsRoom) {
            client.sendMsgToSender(msg);
        }
    }

    public List<Client> getClientsRoom() {
        return clientsRoom;
    }

    public void setClientsRoom(List<Client> clientsRoom) {
        this.clientsRoom = clientsRoom;
    }

    public List<Deque<String>> getDictionaryClients() {
        return dictionaryClients;
    }

    public void setDictionaryClients(List<Deque<String>> dictionaryClients) {
        this.dictionaryClients = dictionaryClients;
    }
}
