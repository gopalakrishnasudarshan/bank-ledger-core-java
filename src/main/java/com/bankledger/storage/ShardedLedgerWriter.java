package com.bankledger.storage;

import com.bankledger.model.Transaction;
import com.bankledger.storage.partition.HashPartitionStrategy;
import com.bankledger.storage.partition.PartitionStrategy;

import java.io.IOException;
import java.nio.file.Path;

public class ShardedLedgerWriter {

    private final ShardRouter router;

    private final  LedgerWriter[] writers;

    public ShardedLedgerWriter(ShardRouter router) {

        this.router = router;

        int n = router.shardCount();
        this.writers = new LedgerWriter[n];

        for(int shard = 0 ; shard <n; shard++) {
            this.writers[shard] = new LedgerWriter(router.shardPath(shard));
        }
    }

    public void append(Transaction transaction) throws IOException {
         int shard = router.shardForAccount(transaction.getAccountId());
         writers[shard].append(transaction);
    }

}
