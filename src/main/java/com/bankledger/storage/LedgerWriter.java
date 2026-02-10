package com.bankledger.storage;

import com.bankledger.model.Transaction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class LedgerWriter {

    private final Path logPath;

    public LedgerWriter(Path logPath) {
        this.logPath = logPath;
    }

   public synchronized void append(Transaction transaction) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(logPath.toFile(),true))){

           writer.write(toLine(transaction));
           writer.newLine();
           writer.flush();
        }
   }

   private String toLine(Transaction transaction) {
        return transaction.getTransactionId()+ "," +
                transaction.getAccountId()+ "," +
                transaction.getTransactionType()+","+
                transaction.getAmount()+"," +
                transaction.getTimestamp();


   }

}
