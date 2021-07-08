package com.siberteam.server;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayDeque;
import java.util.Collection;

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
            sendMsgToClient("Добро пожаловать, пожалуйста, пройдите регистрацию (/register nickname)");
            while (isNonExit) {
                Transfer transfer = new Gson().fromJson(deserialization.readLine(), Transfer.class);
                if (!transfer.getClientActions().equals(ClientActions.REGISTER_SERVER) && isAuth()) {
                    continue;
                }
                switch (transfer.getClientActions()) {
                    case REGISTER_SERVER:
                        setNickName(transfer.getMessage());
                        sendMsgToClient(Color.ANSI_GREEN.paint("Вы успешно зарегистрировались в системе"));
                        break;
                    case CREATE_ROOM:
                        if (room == null) {
                            room = new Room();
                            room.addClientIntoRoom(this);
                            sendMsgToClient(server.createRoom(room));
                        } else {
                            sendMsgToClient(Color.ANSI_RED.paint("Вы уже являетесь участником группы"));
                        }
                        break;
                    case CONNECT_ROOM:
                        if (room == null) {
                            room = server.searchRoom(Integer.parseInt(transfer.getMessage()));
                            if (room != null) {
                                room.addClientIntoRoom(this);
                            } else {
                                sendMsgToClient("Комнаты с номером " + transfer.getMessage() + " не существует");
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
        } catch (SocketException se){
                LoggerError.log("Пользователь " + nickName + " принудительно закрыл программу");
                server.removeClient(this);
                isNonExit = false;
        }
        catch (Exception e) {
            LoggerError.log(e.getMessage());
        }
    }

    public void sendMsgToClient(String msg) throws IOException {
        serialization.write(new Gson().toJson(new AnswerServerTransfer(msg, null)));
        serialization.newLine();
        serialization.flush();
    }

    public void sendMsgWithCollectionToSender(String msg, Collection<String> collection) throws IOException {
        serialization.write(new Gson().toJson(new AnswerServerTransfer(msg, new ArrayDeque<>(collection))));
        serialization.newLine();
        serialization.flush();
    }

    public boolean isAuth() throws IOException {
        if (nickName == null) {
            sendMsgToClient("Пожалуйста, пройдите регистрацию (/register nickname)");
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
