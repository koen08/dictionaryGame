package com.siberteam.game.clients;

import com.google.gson.Gson;
import com.siberteam.game.clients.AnswerServerTransfer;
import com.siberteam.game.clients.Transfer;

import java.io.*;
import java.net.Socket;

public class Client {
    public static final int PORT = 8000;
    private final Socket socket;
    BufferedReader deserialization;
    BufferedWriter serialization;

    public Client() throws IOException {
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
