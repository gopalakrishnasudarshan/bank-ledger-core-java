package com.bankledger.client;

import com.bankledger.model.Transaction;
import com.bankledger.storage.LedgerWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Path;

import static com.bankledger.model.TransactionType.DEPOSIT;

public class BankClient {

    private static final String HOST = "localhost";
    private static final int PORT = 9090;

    public static void main(String[] args) throws IOException {

//        Transaction transaction = new Transaction(1234L,"ACC01", DEPOSIT, 50L,System.currentTimeMillis());
//        Path logPath = Path.of("transactions.log");
//        LedgerWriter ledgerWriter = new LedgerWriter(logPath);
//
//        try {
//            ledgerWriter.append(transaction);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

       for(int i = 1; i<=5 ; i++)
       {
           final int clientId = i;
           new Thread(() -> {
               try (Socket socket = new Socket(HOST, PORT);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                   String request = "ACC0" + clientId + ",DEPOSIT," + (clientId * 10);
                   out.println(request);

                   String response = in.readLine();
                   System.out.println("Client " + clientId + " response: " + response);

               } catch (IOException e) {
                   e.printStackTrace();
               }
           }).start();


       }
    }
}

