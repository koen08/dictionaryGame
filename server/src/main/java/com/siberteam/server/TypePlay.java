package com.siberteam.server;

import java.util.HashMap;
import java.util.Map;

public enum TypePlay {
    AUTO_PLAY("auto"),
    MANUAL_PLAY("manual");
    String typePlayString;
    public static Map<String, TypePlay> TYPE_PLAY_MAP = new HashMap<>();

    TypePlay(String typePlayString) {
        this.typePlayString = typePlayString;
    }

    static {
        for (TypePlay typePlay : values()) {
            TYPE_PLAY_MAP.put(typePlay.typePlayString, typePlay);
        }
    }

    public static TypePlay getEnumFromCommand(String command) {
        return TYPE_PLAY_MAP.get(command);
    }

}
