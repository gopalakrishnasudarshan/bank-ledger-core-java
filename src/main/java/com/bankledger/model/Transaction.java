package com.bankledger.model;

import java.util.Objects;

public class Transaction {


    /**
     * Transaction represents a single banking event
     * All fields are immutable(final) because transaction must not change once created
     */

    private final long transactionId;
    private final String accountId;
    private final TransactionType transactionType;
    private final long amount;
    private final long timestamp;

    public Transaction(long transactionId, String accountId, TransactionType transactionType, long amount, long timestamp) {
        if (accountId == null || accountId.isEmpty()) {
            throw new IllegalArgumentException("accountId is  must not be null or empty");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than 0");
        }
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.transactionType = Objects.requireNonNull(transactionType, "transactionType is  must not be null");
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public long getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" + "transactionId=" + transactionId + ", accountId='" +
                accountId + '\'' + ", transactionType='" + transactionType + '\''
                + ", amount=" + amount + ", timestamp=" + timestamp + '}';
    }
}
