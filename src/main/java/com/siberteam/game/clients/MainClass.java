package com.siberteam.game.clients;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class MainClass {

    public static void main(String[] args) {
        try {
            outputMessageConsole("Введите команду /help, чтобы посмотреть возможные команды");
            Client client = new Client();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            MessageReaderThread messageReaderThread = new MessageReaderThread(client);
            messageReaderThread.start();
            StringBuilder lineCommand = new StringBuilder();
            while (!lineCommand.append(reader.readLine()).toString().equals("/exit")) {
                if (lineCommand.toString().equals("/help")){
                    outputHelper();
                    lineCommand.setLength(0);
                    continue;
                }
                Transfer transfer = MainClass.splitStringToken(lineCommand.toString());
                if (transfer.getClientActions() != null){
                    if (transfer.getClientActions().equals(ClientActions.DOWNLOAD_DICTIONARY)) {
                        transfer.setDictionaryWords(MainClass.formingDequeFromFileDictionary(transfer.getMessage()));
                    }
                    client.sendMsgToServer(transfer);
                    lineCommand.setLength(0);
                } else{
                    outputMessageConsole(Color.ANSI_RED.paint(
                            "Такой команды нет. Введите /help, чтобы посмотреть список команд"));
                }

            }
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

    public static Deque<String> formingDequeFromFileDictionary(String path) {
        Deque<String> dictionary = new ArrayDeque<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String word;
            while ((word = bufferedReader.readLine()) != null) {
                dictionary.add(word);
            }
        } catch (Exception e) {
            LoggerError.log("Проблема загрзуки словаря, проверьте путь к файлу...");
        }
        return dictionary;
    }
}
