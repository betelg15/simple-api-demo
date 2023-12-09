package com.example.demo.infra.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class SimpleRoutingDataSource extends AbstractRoutingDataSource {

    public static final String MAIN = "main";
    public static final String REPLICA = "replica";

    @Override
    protected Object determineCurrentLookupKey() {
        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            return REPLICA;
        } else {
            return MAIN;
        }
    }
}
