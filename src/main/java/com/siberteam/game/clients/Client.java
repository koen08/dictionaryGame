package com.siberteam.game.clients;

import com.google.gson.Gson;
import com.siberteam.game.server.AnswerServerTransfer;
import com.siberteam.game.server.Transfer;

import java.io.*;
import java.net.Socket;

public class Client {
    public static final int PORT = 8000;
    private final Socket socket;

    public Client() throws IOException {
        socket = new Socket("localhost", PORT);
    }

    public void sendMsgToServer(Transfer transfer) throws IOException {
        try (BufferedWriter serialization = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            serialization.write(new Gson().toJson(transfer));
        }
    }

    public AnswerServerTransfer readerMsgFromServer() throws IOException, ClassNotFoundException {
        try (ObjectInputStream deserialization = new ObjectInputStream(new FileInputStream("answer"))) {
            return (AnswerServerTransfer) deserialization.readObject();
        }
    }
}
