package com.bankledger.storage;

import com.bankledger.storage.partition.PartitionStrategy;

import java.nio.file.Path;
import java.util.Objects;

public class ShardRouter {

    private final Path baseDir;
    private final String filePrefix;
    private final PartitionStrategy partitionStrategy;

    public ShardRouter(Path baseDir, String filePrefix, PartitionStrategy partitionStrategy) {

        this.baseDir = Objects.requireNonNull(baseDir, "baseDir");
        this.filePrefix = Objects.requireNonNull(filePrefix, "filePrefix");
        this.partitionStrategy = Objects.requireNonNull(partitionStrategy, "partitionStrategy");
    }

    public int shardCount() {
        return partitionStrategy.shardCount();
    }

    public int shardForAccount(String accountId){
        return partitionStrategy.shardFor(accountId);
    }

    public Path shardPath(int shardId) {
        if(shardId < 0 || shardId >= shardCount()){

            throw new IllegalArgumentException("Invalid shardId: " + shardId);
        }
        return baseDir.resolve(filePrefix + "_" + shardId + ".log");
    }

}
