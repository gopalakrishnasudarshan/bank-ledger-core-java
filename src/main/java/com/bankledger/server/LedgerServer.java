package com.bankledger.server;

import com.bankledger.model.Transaction;
import com.bankledger.model.TransactionType;
import com.bankledger.storage.LedgerWriter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class LedgerServer {

    private static final int PORT = 9090;
    private static final Path LOG_PATH = Path.of("transactions.log");
    private static final AtomicLong TXN_ID_SEQ = new AtomicLong(1);
    private static final ExecutorService pool = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws IOException {

        LedgerWriter ledgerWriter = new LedgerWriter(LOG_PATH);

        // When the application is terminated , this makes sure the thread pool is shut down gracefully.
        //To make sure that the worker threads are not left running and ensures server exits cleanly.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            pool.shutdown();
        }));

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Ledgerserver is listening on port " + PORT);



            while (true) {
                Socket client = serverSocket.accept();
                pool.submit(() ->

                        handleClient(client, ledgerWriter));
            }
        }


    }

    private static void handleClient(Socket client, LedgerWriter ledgerWriter) {
        try (client;

             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

            String line = in.readLine();
            if (line == null || line.isBlank()) {
                out.println("ERROR,EMPTY_REQUEST");
                return;
            }
            if(line.startsWith("READ,"))
            {
                handleRead(line,out);
                return;
            }

            try {
                Transaction txn = parseToTransaction(line);
                ledgerWriter.append(txn);

                System.out.println("Transaction Saved: " + txn);
                out.println("OK," + txn.getTransactionId());
            } catch (IllegalArgumentException e) {
                out.println("ERROR, invalid BAD_REQUEST");
            }

        } catch (IOException e) {
            e.printStackTrace();
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

    private static void handleRead(String line, PrintWriter out) {
        String [] parts = line.split(",");
        if(parts.length != 3)
        {
            out.println("ERROR, BAD_READ_REQUEST");
            return;
        }

        int offset;
        int maxLines;
        try {
            offset = Integer.parseInt(parts[1].trim());
            maxLines = Integer.parseInt(parts[2].trim());
        }catch (NumberFormatException e)
        {
            out.println("ERROR, BAD_READ_REQUEST");
            return;
        }

        if(offset < 0 || maxLines < 0)
        {
            out.println("ERROR, BAD_READ_REQUEST");
            return;
        }
        String [] buffer = new String[maxLines];

        int sent = 0 ;
        try(BufferedReader reader = new BufferedReader(new FileReader(LOG_PATH.toFile()))) {

            for (int i = 0 ; i < offset ;i++)
            {
                if(reader.readLine() == null)
                {
                    out.println("OK,READ," + offset);
                    out.println("END");
                    return;
                }
            }

            String txnLine;
            while(sent < maxLines &&(txnLine = reader.readLine()) != null){
               buffer[sent++] = txnLine;
            }

            int nextOffset = offset + sent;
            out.println("OK,READ," + nextOffset);
            for (int i = 0; i < sent; i++) {
                out.println(buffer[i]);
            }
            out.println("END");


        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
