package com.siberteam.game.clients;

import com.siberteam.game.server.AnswerServerTransfer;
import com.siberteam.game.server.Transfer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainClass {
    public static void main(String[] args) {
        try {
            Client client = new Client();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String lineCommand = "";
            while (true) {
                lineCommand = reader.readLine();
                ClientActions clientActions = ClientActions.getEnumFromCommand(lineCommand);
                Transfer transfer = new Transfer();
                transfer.setClientActions(clientActions);
                client.sendMsgToServer(transfer);
              //  AnswerServerTransfer answerServerTransfer = client.readerMsgFromServer();
             //   System.out.println(answerServerTransfer.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
