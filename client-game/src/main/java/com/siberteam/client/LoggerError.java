package com.siberteam.client;

public class LoggerError {
    public static void log(String messageLog) {
        System.out.println(Color.ANSI_RED.paint(messageLog));
    }
}
