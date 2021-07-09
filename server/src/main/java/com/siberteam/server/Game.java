package com.siberteam.server;

import java.io.IOException;

public interface Game {
    void startGame(TypePlay typePlay) throws IOException, InterruptedException;

    boolean isOneWinner();

    Client searchWinner();

    int getRandomIndexWithRepeat(int player);
}
