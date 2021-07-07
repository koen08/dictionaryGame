package com.siberteam.server;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.Deque;

public class Client extends Thread {
    private final Socket socket;
    private final Server server;
    private final BufferedReader deserialization;
    private final BufferedWriter serialization;
    private String nickName;
    private Room room = null;
    boolean isNonExit = true;

    public Client(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        deserialization = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        serialization = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        try {
            sendMsgToSender("Добро пожаловать, пожалуйста, пройдите регистрацию (/register nickname)");
            while (isNonExit) {
                Transfer transfer = new Gson().fromJson(deserialization.readLine(), Transfer.class);
                if (!transfer.getClientActions().equals(ClientActions.REGISTER_SERVER) && isAuth()) {
                    continue;
                }
                switch (transfer.getClientActions()) {
                    case REGISTER_SERVER:
                        setNickName(transfer.getMessage());
                        sendMsgToSender(Color.ANSI_GREEN.paint("Вы успешно зарегистрировались в системе"));
                        break;
                    case CREATE_ROOM:
                        if (room == null) {
                            room = new Room();
                            room.addClientIntoRoom(this);
                            sendMsgToSender(server.createRoom(room));
                        } else sendMsgToSender(Color.ANSI_RED.paint("Вы уже являетесь участником группы"));
                        break;
                    case CONNECT_ROOM:
                        if (room == null) {
                            room = server.searchRoom(Integer.parseInt(transfer.getMessage()));
                            if (room != null) {
                                room.addClientIntoRoom(this);
                            } else {
                                sendMsgToSender("Комнаты с номером " + transfer.getMessage() + " не существует");
                            }
                        }
                        break;
                    case DELETE_ROOM:
                        if (room != null) {
                            room.deleteRoom(this);
                        }
                        break;
                    case EXIT_ROOM:
                        if (room != null) {
                            room.deleteClientFromRoom(this);
                        }
                        break;
                    case START_GAME:
                        room.startGame(this);
                        break;
                    case DOWNLOAD_DICTIONARY:
                        if (transfer.getDictionaryWords() != null) {
                            room.downloadDictionary(this, transfer.getDictionaryWords());
                        }
                        break;
                    case MADE_MOVE:
                        room.addWordToSlot(this, transfer.getMessage());
                        break;
                    case EXIT_GAME:
                        server.removeClient(this);
                        LoggerError.log("Пользователь " + nickName + " отключился от сервера");
                        isNonExit = false;
                        socket.close();
                        serialization.close();
                        deserialization.close();
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            if (e.getMessage() == null) {
                LoggerError.log("Пользователь " + nickName + " принудительно закрыл программу");
                server.removeClient(this);
                isNonExit = false;
            }
            LoggerError.log(e.getMessage());
        }
    }

    public void sendMsgToSender(String msg) throws IOException {
        serialization.write(new Gson().toJson(new AnswerServerTransfer(msg, null)));
        serialization.newLine();
        serialization.flush();
    }

    public void sendMsgWithDequeToSender(String msg, Deque<String> deque) throws IOException {
        serialization.write(new Gson().toJson(new AnswerServerTransfer(msg, deque)));
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
