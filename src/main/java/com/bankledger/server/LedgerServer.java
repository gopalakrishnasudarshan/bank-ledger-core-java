package com.bankledger.server;

import com.bankledger.model.Transaction;
import com.bankledger.model.TransactionType;
import com.bankledger.storage.LedgerWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;

public class LedgerServer {

    private static final int PORT = 9090;
    private static final Path LOG_PATH = Path.of("transactions.log");
    private static final AtomicLong TXN_ID_SEQ = new AtomicLong(1);

    public static void main(String[] args) throws IOException {

        LedgerWriter ledgerWriter = new LedgerWriter(LOG_PATH);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Ledgerserver is listening on port " + PORT);

            while (true) {
                Socket client = serverSocket.accept();
                handleClient(client, ledgerWriter);
            }
        }


    }

    private static void handleClient(Socket client, LedgerWriter ledgerWriter) throws IOException {
        try (client;

             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

            String line = in.readLine();
            if (line == null || line.isBlank()) {
                out.println("ERROR, empty_request");
                return;
            }

            Transaction txn = parseToTransaction(line);
            ledgerWriter.append(txn);

            System.out.println("Transaction Saved: " + txn);
            out.println("OK, " + txn.getTransactionId());

        }
    }

    private static Transaction parseToTransaction(String line) {
        String[] parts = line.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid request format. Expected: accountId,type,amount");
        }

        String accountId = parts[0].trim();
        TransactionType type = TransactionType.valueOf(parts[1].trim().toUpperCase());
        long amount = Long.parseLong(parts[2].trim());

        long txnId = TXN_ID_SEQ.getAndIncrement();
        long timestamp = System.currentTimeMillis();

        return new Transaction(txnId, accountId, type, amount, timestamp);
    }


}
