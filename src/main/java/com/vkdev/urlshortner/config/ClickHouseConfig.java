package com.vkdev.urlshortner.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ClickHouseConfig {
    @Bean
    @ConfigurationProperties(prefix = "datasources.clickhouse.hikari")
    public HikariConfig clickhouseHikariConfig() {
        return new HikariConfig();
    }

    @Bean(name = "clickhouseDataSource")
    public DataSource clickhouseDataSource(
            @Value("${datasources.clickhouse.jdbc-url}") String url,
            @Value("${datasources.clickhouse.username}") String user,
            @Value("${datasources.clickhouse.password}") String password,
            @Qualifier("clickhouseHikariConfig") HikariConfig hikariConfig) {
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);
        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "clickhouseJdbcTemplate")
    public JdbcTemplate clickhouseJdbcTemplate(
            @Qualifier("clickhouseDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
