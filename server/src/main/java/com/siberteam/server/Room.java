package com.siberteam.server;

import java.io.IOException;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

public class Room {
    static final int INDEX_CREATOR = 0;
    private List<Client> clientsRoom = new CopyOnWriteArrayList<>();
    private List<Deque<String>> dictionaryClients = new CopyOnWriteArrayList<>();
    private List<StringBuilder> slotClients = new CopyOnWriteArrayList<>();
    private boolean isGameStart = false;

    public void startGame(Client client) throws IOException, InterruptedException {
        if (client.equals(clientsRoom.get(INDEX_CREATOR))) {
            if (clientsRoom.size() > 1) {
                isGameStart = true;
                Game game = new CarbonCopyWordGame(this);
                if (isAllReadyWithDictionary() && isUniquenessDictionaries()) {
                    game.startGame();
                }
                isGameStart = false;
            } else client.sendMsgToSender(Color.ANSI_RED.paint("Комната должна иметь хотя бы 2 участника"));
        } else client.sendMsgToSender(Color.ANSI_RED.paint("Вы не явялетесь создателем комнаты"));
    }

    private boolean isUniquenessDictionaries() throws IOException {
        for (int i = 0; i < clientsRoom.size() - 1; i++) {
            for (int j = i + 1; j < clientsRoom.size(); j++) {
                for (String s : dictionaryClients.get(i)) {
                    if (dictionaryClients.get(j).contains(s)) {
                        messageRoom(Color.ANSI_RED.paint("Слово - " + s + " - встречается у игрока " +
                                clientsRoom.get(i).getNickName() + " и " +
                                clientsRoom.get(j).getNickName()));
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void addClientIntoRoom(Client client) throws IOException {
        if (!clientsRoom.contains(client) && !isGameStart) {
            clientsRoom.add(client);
            dictionaryClients.add(new ConcurrentLinkedDeque<>());
            slotClients.add(new StringBuilder());
            messageRoom("Пользователь " + client.getNickName() + " подключился к комнате");
            messageRoom("Количество игроков в комнате - " + clientsRoom.size());
        }
    }

    public void deleteClientFromRoom(Client client) throws IOException {
        if (!isGameStart) {
            clientsRoom.remove(client);
            client.setRoom(null);
            client.sendMsgToSender("Вы покинули группу");
            messageRoom(client.getNickName() + " покинул комнату" + ", размер комнаты - " + clientsRoom.size());
        }
    }

    public void deleteRoom(Client client) throws IOException {
        if (client.equals(clientsRoom.get(INDEX_CREATOR)) && !isGameStart) {
            for (Client cl : clientsRoom) {
                cl.sendMsgToSender("Комната была удалена, вы вернулись в главное меню");
                cl.setRoom(null);
            }
        } else client.sendMsgToSender("Вы не явялетесь создателем комнаты");
    }

    public void downloadDictionary(Client client, Deque<String> deque) throws IOException {
        if (!isGameStart) {
            int indexClientsIntoRoom = clientsRoom.indexOf(client);
            deleteAllWords(indexClientsIntoRoom);
            while (!deque.isEmpty()) {
                dictionaryClients.get(indexClientsIntoRoom).add(deque.pop());
            }
            client.sendMsgToSender(Color.ANSI_GREEN.paint("Вы успешно загрузили свой словарь"));
            messageRoom(Color.ANSI_GREEN.paint("Игрок с ником " +
                    client.getNickName() + " загрузил свой словарь и готов к игре!"));
        }
    }

    private void deleteAllWords(int index) {
        while (!dictionaryClients.get(index).isEmpty()) {
            dictionaryClients.get(index).pop();
        }
    }

    public boolean isAllReadyWithDictionary() throws IOException {
        for (Client client : clientsRoom) {
            int indexClientsIntoRoom = clientsRoom.indexOf(client);
            if (dictionaryClients.get(indexClientsIntoRoom).isEmpty()) {
                messageRoom(Color.ANSI_RED.paint("Игрок " + client.getNickName() + " не загрузил словарь"));
                return false;
            }
        }
        return true;
    }

    public void addWordToSlot(Client client, String word) {
        int indexClient = clientsRoom.indexOf(client);
        slotClients.get(indexClient).append(word);
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

    public List<StringBuilder> getSlotClients() {
        return slotClients;
    }
}
