package com.example.demo.infra.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@ConditionalOnProperty(prefix = "demo.datasource.main", name = {"driver-class-name"})
@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.demo",
        entityManagerFactoryRef = DemoDbConfig.ENTITY_MANAGER,
        transactionManagerRef = DemoDbConfig.TRANSACTION_MANAGER
)
public class DemoDbConfig {

    public final static String ENTITY_MANAGER = "demoEntityManager";
    public final static String TRANSACTION_MANAGER = "demoTransactionManager";
    public final static String MAIN_DATA_SOURCE = "demoMainDataSource";
    public final static String MAIN_DATA_SOURCE_PROP = "demoMainDataSourceProp";
    public final static String REPLICA_DATA_SOURCE = "demoReplicaDataSource";
    public final static String REPLICA_DATA_SOURCE_PROP = "demoReplicaDataSourceProp";
    public final static String ROUTING_DATA_SOURCE = "demoRoutingDataSource";

    @Primary
    @Bean(ENTITY_MANAGER)
    public LocalContainerEntityManagerFactoryBean entityManager(
            @Qualifier(ROUTING_DATA_SOURCE) DataSource dataSource
    ) {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(dataSource);
        entityManager.setPackagesToScan("com.example.demo");
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return entityManager;
    }

    @Primary
    @Bean(MAIN_DATA_SOURCE_PROP)
    @ConfigurationProperties(prefix = "demo.datasource.main")
    public HikariConfig mainDataSourceProperties()  {
        return new HikariConfig();
    }

    @Primary
    @Bean(MAIN_DATA_SOURCE)
    public DataSource mainDataSource(@Qualifier(MAIN_DATA_SOURCE_PROP) HikariConfig dataSourceProperties) {
        return new HikariDataSource(dataSourceProperties);
    }

    @Bean(REPLICA_DATA_SOURCE_PROP)
    @ConfigurationProperties(prefix = "demo.datasource.replica")
    public HikariConfig replicaDataSourceProperties()  {
        return new HikariConfig();
    }

    @Bean(REPLICA_DATA_SOURCE)
    public DataSource replicaDataSource(@Qualifier(REPLICA_DATA_SOURCE_PROP) HikariConfig dataSourceProperties) {
        return new HikariDataSource(dataSourceProperties);
    }

    @Bean(ROUTING_DATA_SOURCE)
    public DataSource routingDataSource(
            @Qualifier(MAIN_DATA_SOURCE) DataSource mainDataSource,
            @Qualifier(REPLICA_DATA_SOURCE) DataSource replicaDataSource
    ) {
        SimpleRoutingDataSource routingDataSource = new SimpleRoutingDataSource();
        routingDataSource.setDefaultTargetDataSource(mainDataSource);
        routingDataSource.setTargetDataSources(
                new HashMap<>() {{
                    put(SimpleRoutingDataSource.MAIN, mainDataSource);
                    put(SimpleRoutingDataSource.REPLICA, replicaDataSource);
                }}
        );
        routingDataSource.afterPropertiesSet();

        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Primary
    @Bean(TRANSACTION_MANAGER)
    public PlatformTransactionManager transactionManager(
            @Qualifier(ENTITY_MANAGER) LocalContainerEntityManagerFactoryBean entityManager
    ) {
        JpaTransactionManager jpaTransactionManager = new LazyAwareJpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManager.getObject());
        return jpaTransactionManager;
    }
}