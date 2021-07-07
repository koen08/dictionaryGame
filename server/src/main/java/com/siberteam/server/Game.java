package com.siberteam.server;

import java.io.IOException;

public interface Game {
    void startGame() throws IOException, InterruptedException;
    boolean isOneWinner();
    Client searchWinner();
    int getRandomIndexWithRepeat(int player);
}
