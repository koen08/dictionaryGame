package com.siberteam.client;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class ClientManager {
    public static final int PORT = 8000;
    private final Socket socket;
    BufferedReader deserialization;
    BufferedWriter serialization;

    public ClientManager() throws IOException {
        socket = new Socket("localhost", PORT);
        deserialization = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        serialization = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void sendMsgToServer(Transfer transfer) throws IOException {
        serialization.write(new Gson().toJson(transfer));
        serialization.newLine();
        serialization.flush();
    }

    public AnswerServerTransfer readerMsgFromServer() throws IOException {
        return new Gson().fromJson(deserialization.readLine(), AnswerServerTransfer.class);
    }
}
