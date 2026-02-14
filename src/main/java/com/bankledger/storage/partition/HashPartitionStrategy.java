package com.bankledger.storage.partition;

import java.util.Objects;

public class HashPartitionStrategy implements PartitionStrategy {

    private final int shardCount;

    public HashPartitionStrategy(int shardCount) {

        if(shardCount <= 0) throw  new IllegalArgumentException("shardCount must be greater than 0");
        this.shardCount = shardCount;
    }

    @Override
    public int shardFor(String accountId) {

        Objects.requireNonNull(accountId, "accountId");
        int h = accountId.hashCode();

        return Math.floorMod(h,shardCount);

    }

    @Override
    public int shardCount() {
      return shardCount;
    }
}
