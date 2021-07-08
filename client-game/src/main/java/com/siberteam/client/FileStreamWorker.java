package com.siberteam.client;

import java.io.*;
import java.util.*;

public class FileStreamWorker {
    public void writeDequeWordIntoFile(Deque<String> deque) throws IOException {
        File file = new File(UUID.randomUUID() + ".txt");
        file.createNewFile();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            while (!deque.isEmpty()) {
                bufferedWriter.write(deque.pop() + System.lineSeparator());
            }
        }
    }

    public List<String> formingDequeFromFileDictionary(String path) {
        List<String> dictionary = new ArrayList<>();
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
