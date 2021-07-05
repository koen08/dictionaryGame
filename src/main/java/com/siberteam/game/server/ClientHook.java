package com.siberteam.game.server;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;

public class ClientHook extends Thread {
    private final Socket socket;
    private final Server server;
    private String nickName;
    private final Deque<String> words;
    private BufferedReader deserialization;
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
            deserialization = new BufferedReader(new InputStreamReader(input));
            while (true) {
                String str = deserialization.readLine();
                System.out.println(str);
                Transfer transfer = new Gson().fromJson(deserialization.readLine(), Transfer.class);
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
        } catch (IOException e) {
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
