package uz.uat.backend.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Objects;
import java.util.Properties;

import static uz.uat.backend.BackendApplication.MODEL_PACKAGE;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryUAT", transactionManagerRef = "transactionManagerUAT", basePackages = {"uz.uat.backend.repository"})
public class DBUAT {
    protected final String PERSISTENCE_UNIT_NAME = "uat";
    protected final Properties JPA_ETRANZIT = new Properties() {{
        put("database-platform", "org.hibernate.dialect.DB2400Dialect");
        put("hibernate.hbm2ddl.auto", "none");
        put("hibernate.dialect", "org.hibernate.dialect.DB2400Dialect");
        put("show-sql", "true");
        put("generate-ddl", "true");
    }};

    @Bean
    @Qualifier("uat")
    public HikariDataSource uat() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setAutoCommit(true);
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        hikariConfig.addDataSourceProperty("encoding", "UTF-8");
        hikariConfig.addDataSourceProperty("useUnicode", "true");
        hikariConfig.setPoolName("uat");
        hikariConfig.setDriverClassName("com.ibm.as400.access.AS400JDBCDriver");
        hikariConfig.setConnectionTestQuery("select current_timestamp cts from sysibm.sysdummy1");
//        hikariConfig.setJdbcUrl("jdbc:as400://baza_url/UAT");
//        hikariConfig.setUsername("login");
//        hikariConfig.setPassword("password");
        hikariConfig.setConnectionTimeout(30000);
        hikariConfig.setValidationTimeout(5000);
        hikariConfig.setMinimumIdle(100);
        hikariConfig.setMaximumPoolSize(1000);
        hikariConfig.setIdleTimeout(600000);
        hikariConfig.setMaxLifetime(3600000);
        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "liquibaseUAT")
    public SpringLiquibase liquibaseUAT(@Qualifier("uat") HikariDataSource uatDataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(uatDataSource);
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
        liquibase.setDatabaseChangeLogTable("DATABASECHANGELOG");
        liquibase.setDatabaseChangeLogLockTable("DATABASECHANGELOGLOCK");
        liquibase.setContexts("uat");
        liquibase.setShouldRun(true);
        liquibase.setDefaultSchema("UAT");
        return liquibase;
    }

    @Primary
    @Bean(name = "entityManagerFactoryUAT")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryUAT(final HikariDataSource uat) {
        return new LocalContainerEntityManagerFactoryBean() {{
            setDataSource(uat);
            setPersistenceProviderClass(HibernatePersistenceProvider.class);
            setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
            setPackagesToScan(MODEL_PACKAGE);
            setJpaProperties(JPA_ETRANZIT);
        }};
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManagerUAT(final @Qualifier("entityManagerFactoryUAT") LocalContainerEntityManagerFactoryBean entityManagerFactoryUAT) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactoryUAT.getObject()));
    }
}

