package com.siberteam.game.clients;

import java.util.HashMap;
import java.util.Map;

public enum ClientActions {
    CREATE_ROOM("/create"),
    REPEAT_GAME("/repeat"),
    DELETE_ROOM("/delete"),
    START_GAME("/start"),
    EXIT_GAME("/exit"),
    CONNECT_ROOM("/connect"),
    MAKE_MOVE("/send");
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
