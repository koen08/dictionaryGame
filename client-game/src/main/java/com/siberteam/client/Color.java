package com.siberteam.client;

public enum Color {
    ANSI_RESET("\u001B[0m") {
        @Override
        public String paint(String msg) {
            return ANSI_RESET.codeColor + msg + ANSI_RESET.codeColor;
        }
    },
    ANSI_RED("\u001B[31m") {
        @Override
        public String paint(String msg) {
            return ANSI_RED.codeColor + msg + ANSI_RESET.codeColor;
        }
    },
    ANSI_GREEN("\u001B[32m") {
        @Override
        public String paint(String msg) {
            return ANSI_GREEN.codeColor + msg + ANSI_RESET.codeColor;
        }
    },
    ANSI_YELLOW("\u001B[33m") {
        @Override
        public String paint(String msg) {
            return ANSI_YELLOW.codeColor + msg + ANSI_RESET.codeColor;
        }
    },
    ANSI_BLUE("\u001B[34m") {
        @Override
        public String paint(String msg) {
            return ANSI_BLUE.codeColor + msg + ANSI_RESET.codeColor;
        }
    };
    private final String codeColor;

    Color(String codeColor) {
        this.codeColor = codeColor;
    }

    public abstract String paint(String msg);
}
