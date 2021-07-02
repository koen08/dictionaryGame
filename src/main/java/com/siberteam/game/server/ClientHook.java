package com.siberteam.game.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;

public class ClientHook extends Thread {
    private final Socket socket;
    private final Server server;
    private String nickName;
    private final Deque<String> words;
    private ObjectInputStream deserialization;
    public ClientHook(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        words = new ArrayDeque<>();
        start();
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            deserialization = new ObjectInputStream(input);
            while (true) {
                Transfer transfer = (Transfer) deserialization.readObject();
                switch (transfer.getClientActions()) {
                    case CREATE_ROOM:
                        Room room = new Room();
                        room.addClientIntoRoom(this);
                        sendMsgToSender(server.createRoom(room));
                        break;
                    case CONNECT_ROOM:
                        Room room1 = server.searchRoom(Integer.parseInt(transfer.getMessage()));
                        if (room1 != null) {
                            room1.addClientIntoRoom(this);
                        } else sendMsgToSender("Комнаты с номером " + transfer.getMessage() + " не существует");
                    default:
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Deque<String> getWords() {
        return words;
    }

    public void sendMsgToSender(String msg) throws IOException {
        try (ObjectOutputStream serialization = new ObjectOutputStream(new FileOutputStream("answers"))) {
            serialization.writeObject(new AnswerServerTransfer(msg));
        }
    }
}
