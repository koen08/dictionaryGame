package com.siberteam.game.clients;

import com.siberteam.game.server.Color;

public class LoggerError {
    public static void log(String messageLog) {
        System.out.println(Color.ANSI_RED.paint(messageLog));
    }
}
