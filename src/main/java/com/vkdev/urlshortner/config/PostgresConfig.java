package com.vkdev.urlshortner.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class PostgresConfig {
    @Bean
    @ConfigurationProperties(prefix = "datasources.postgres.hikari")
    public HikariConfig postgresHikariConfig() {
        return new HikariConfig();
    }

    @Bean(name = "postgresDataSource")
    @Primary  // mark Postgres as default for any unannotated injections
    public DataSource postgresDataSource(
            @Value("${datasources.postgres.jdbc-url}") String url,
            @Value("${datasources.postgres.username}") String user,
            @Value("${datasources.postgres.password}") String password,
            HikariConfig postgresHikariConfig) {
        postgresHikariConfig.setJdbcUrl(url);
        postgresHikariConfig.setUsername(user);
        postgresHikariConfig.setPassword(password);
        return new HikariDataSource(postgresHikariConfig);
    }

    @Bean(name = "postgresJdbcTemplate")
    @Primary
    public JdbcTemplate postgresJdbcTemplate(
            @Qualifier("postgresDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean(name = "postgresTransactionManager")
    @Primary
    public PlatformTransactionManager postgresTransactionManager(
            @Qualifier("postgresDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    // Flyway runs against Postgres only
    @Bean(initMethod = "migrate")
    public Flyway flyway(@Qualifier("postgresDataSource") DataSource ds) {
        return Flyway.configure()
                .dataSource(ds)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
    }
}
