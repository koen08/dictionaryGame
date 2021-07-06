package com.siberteam.client;

import java.util.HashMap;
import java.util.Map;

public enum ClientActions {
    REGISTER_SERVER("/register"),
    CREATE_ROOM("/create"),
    DELETE_ROOM("/delete"),
    EXIT_ROOM("/exit_room"),
    START_GAME("/start"),
    EXIT_GAME("/exit"),
    CONNECT_ROOM("/connect"),
    DOWNLOAD_DICTIONARY("/download");
    public static Map<String, ClientActions> CLIENT_ACTIONS_MAP = new HashMap<>();
    private final String command;

    ClientActions(String command) {
        this.command = command;
    }

    static {
        for (ClientActions clientActions : values()) {
            CLIENT_ACTIONS_MAP.put(clientActions.command, clientActions);
        }
    }

    public static ClientActions getEnumFromCommand(String command) {
        return CLIENT_ACTIONS_MAP.get(command);
    }

    public String getCommand() {
        return command;
    }
}
