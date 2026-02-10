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

        try(Socket socket = new Socket(HOST,PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())))
            {
              out.println("ACC01,DEPOSIT,50");

                String response = in.readLine();
                System.out.println("Server response: " + response);
            }

    }
}

