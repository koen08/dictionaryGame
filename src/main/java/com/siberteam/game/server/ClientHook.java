package com.siberteam.game.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;

public class ClientHook extends Thread {
    private Socket socket;
    private String nickName;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Deque<String> words;

    public ClientHook(Socket socket) throws IOException {
        this.socket = socket;
        words = new ArrayDeque<>();
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        try {
            String word;
            while (!(word = bufferedReader.readLine()).equals("DS{JL")) {
                words.add(word);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Deque<String> getWords() {
        return words;
    }
}
