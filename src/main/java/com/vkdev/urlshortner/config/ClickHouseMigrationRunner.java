package com.vkdev.urlshortner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@Slf4j
public class ClickHouseMigrationRunner implements ApplicationRunner {

    @Autowired
    @Qualifier("clickhouseJdbcTemplate")
    private JdbcTemplate clickhouseJdbc;

    private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Track applied migrations in ClickHouse itself
        clickhouseJdbc.execute("""
            CREATE TABLE IF NOT EXISTS schema_migrations (
                version String,
                applied_at DateTime DEFAULT now()
            ) ENGINE = MergeTree ORDER BY version
            """);

        Set<String> applied = new HashSet<>(
                clickhouseJdbc.queryForList("SELECT version FROM schema_migrations", String.class));

        Resource[] migrations = resolver.getResources("classpath:clickhouse/migration/V*.sql");
        Arrays.sort(migrations, Comparator.comparing(Resource::getFilename));

        for (Resource migration : migrations) {
            String version = extractVersion(migration.getFilename());
            if (applied.contains(version)) continue;

            log.info("Applying ClickHouse migration: {}", migration.getFilename());
            String sql = new String(migration.getInputStream().readAllBytes(), UTF_8);

            // ClickHouse needs statements run one at a time
            for (String statement : splitStatements(sql)) {
                if (!statement.isBlank()) {
                    clickhouseJdbc.execute(statement);
                }
            }
            clickhouseJdbc.update(
                    "INSERT INTO schema_migrations (version) VALUES (?)", version);
        }
    }

    private String extractVersion(String filename) {
        return filename.substring(0, filename.indexOf("__"));
    }

    private List<String> splitStatements(String sql) {
        return Arrays.stream(sql.split(";"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }
}
