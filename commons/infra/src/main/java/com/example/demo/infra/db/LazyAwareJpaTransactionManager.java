package com.example.demo.infra.db;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class LazyAwareJpaTransactionManager extends JpaTransactionManager {

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        Integer isolationLevel = null;

        if (definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT) {
            isolationLevel = definition.getIsolationLevel();
        }

        TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(
            isolationLevel
        );

        TransactionSynchronizationManager.setCurrentTransactionReadOnly(definition.isReadOnly());
        TransactionSynchronizationManager.setCurrentTransactionName(definition.getName());

        super.doBegin(transaction, definition);
    }
}
