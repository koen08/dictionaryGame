package com.siberteam.server;

public class LoggerError {
    public static void log(String messageLog) {
        System.out.println(Color.ANSI_RED.paint(messageLog));
    }
}
