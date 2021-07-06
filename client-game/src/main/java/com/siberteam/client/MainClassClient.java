package com.siberteam.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainClassClient {

    public static void main(String[] args) {
        try {
            outputMessageConsole("Введите команду /help, чтобы посмотреть возможные команды");
            ClientManager client = new ClientManager();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            MessageReaderThread messageReaderThread = new MessageReaderThread(client);
            messageReaderThread.start();
            StringBuilder lineCommand = new StringBuilder();
            while (!lineCommand.append(reader.readLine()).toString().equals("/exit")) {
                if (lineCommand.toString().equals("/help")) {
                    outputHelper();
                    lineCommand.setLength(0);
                    continue;
                }
                Transfer transfer = MainClassClient.splitStringToken(lineCommand.toString());
                if (transfer.getClientActions() != null) {
                    if (transfer.getClientActions().equals(ClientActions.DOWNLOAD_DICTIONARY)) {
                        transfer.setDictionaryWords(
                                new FileStreamWorker().formingDequeFromFileDictionary(transfer.getMessage()));
                    }
                    client.sendMsgToServer(transfer);
                    lineCommand.setLength(0);
                } else {
                    outputMessageConsole(Color.ANSI_RED.paint(
                            "Такой команды нет. Введите /help, чтобы посмотреть список команд"));
                }
            }
            client.sendMsgToServer(new Transfer(ClientActions.EXIT_GAME, ""));
            outputMessageConsole("Вы отключились от сервера");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void outputHelper() {
        System.out.println(Color.ANSI_BLUE.paint("/register [nick_name] - регистрация в игре"));
        System.out.println(Color.ANSI_BLUE.paint("/create - создание комнаты"));
        System.out.println(Color.ANSI_BLUE.paint("/delete - удаление комнаты"));
        System.out.println(Color.ANSI_BLUE.paint("/connect [id_room] - подключение к комнате"));
        System.out.println(Color.ANSI_BLUE.paint("/start - запуск игры"));
        System.out.println(Color.ANSI_BLUE.paint("/exit - отключиться от сервера"));
        System.out.println(Color.ANSI_BLUE.paint("/download [path] - загрузить словарь в игру"));
    }

    public static void outputMessageConsole(String msg) {
        System.out.println(msg);
    }

    public static Transfer splitStringToken(String lineCommand) {
        String[] args = lineCommand.split(" ");
        if (args.length == 1) {
            return new Transfer(ClientActions.getEnumFromCommand(args[0]), null);
        }
        return new Transfer(ClientActions.getEnumFromCommand(args[0]), args[1]);
    }


}
