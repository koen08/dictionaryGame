package com.siberteam.game.server;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;

public class Client extends Thread {
    private final Socket socket;
    private final Server server;
    private final Deque<String> words;
    private final BufferedReader deserialization;
    private final BufferedWriter serialization;
    private String nickName;
    private Room room = null;

    public Client(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        words = new ArrayDeque<>();
        deserialization = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        serialization = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        try {
            sendMsgToSender("Добро пожаловать, пожалуйста, пройдите регистрацию (/register nickname)");
            boolean isNonExit = true;
            while (isNonExit) {
                Transfer transfer = new Gson().fromJson(deserialization.readLine(), Transfer.class);
                switch (transfer.getClientActions()) {
                    case REGISTER_SERVER:
                        setNickName(transfer.getMessage());
                        sendMsgToSender("Вы успешно зарегистрировались в системе");
                        break;
                    case CREATE_ROOM:
                        if (isAuth()) {
                            break;
                        }
                        if (room == null) {
                            room = new Room();
                            room.addClientIntoRoom(this);
                            sendMsgToSender(server.createRoom(room));
                        } else sendMsgToSender("Вы уже являетесь участником группы");
                        break;
                    case CONNECT_ROOM:
                        if (isAuth()) {
                            break;
                        }
                        if (room == null) {
                            room = server.searchRoom(Integer.parseInt(transfer.getMessage()));
                            if (room != null) {
                                room.addClientIntoRoom(this);
                            } else
                                sendMsgToSender("Комнаты с номером " + transfer.getMessage() + " не существует");
                        }
                        break;
                    case DELETE_ROOM:
                        if (isAuth()) {
                            break;
                        }
                        if (room != null) {
                            room.deleteRoom(this);
                        }
                        break;
                    case EXIT_ROOM:
                        if (isAuth()) {
                            break;
                        }
                        if (room != null) {
                            room.deleteClientFromRoom(this);
                        }
                        break;
                    case START_GAME:
                        if (isAuth()) {
                            break;
                        }
                        room.startGame(this);
                        break;
                    case DOWNLOAD_DICTIONARY:
                        if (isAuth()) {
                            break;
                        }
                        room.downloadDictionary(this, transfer.getDictionaryWords());
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsgToSender(String msg) throws IOException {
        serialization.write(new Gson().toJson(new AnswerServerTransfer(msg)));
        serialization.newLine();
        serialization.flush();
    }

    public boolean isAuth() throws IOException {
        if (nickName == null) {
            sendMsgToSender("Пожалуйста, пройдите регистрацию (/register nickname)");
            return true;
        }
        return false;
    }

    public Deque<String> getWords() {
        return words;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Socket getSocket() {
        return socket;
    }

    public Server getServer() {
        return server;
    }

    public BufferedReader getDeserialization() {
        return deserialization;
    }

    public BufferedWriter getSerialization() {
        return serialization;
    }


}
