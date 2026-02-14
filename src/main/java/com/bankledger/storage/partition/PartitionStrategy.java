package com.bankledger.storage.partition;

public interface PartitionStrategy {

    int shardFor(String accountId);
    int shardCount();
}
