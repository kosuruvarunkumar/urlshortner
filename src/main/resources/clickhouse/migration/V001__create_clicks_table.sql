CREATE TABLE IF NOT EXISTS clicks
(
    event_id         UUID,
    code             String,
    occurred_at      DateTime64(3, 'UTC')  CODEC(Delta, ZSTD(1)),
    ip_anonymized    String,
    country_code     LowCardinality(String),
    region           LowCardinality(String),
    city             String,
    browser          LowCardinality(String),
    browser_version  LowCardinality(String),
    os               LowCardinality(String),
    os_version       LowCardinality(String),
    device_type      LowCardinality(String),
    referrer_url     String,
    referrer_domain  LowCardinality(String),
    accept_language  LowCardinality(String),
    user_agent_raw   String  CODEC(ZSTD(3)),
    ingested_at      DateTime  DEFAULT now()
)
ENGINE = ReplacingMergeTree(ingested_at)
PARTITION BY toYYYYMM(occurred_at)
ORDER BY (code, occurred_at, event_id)
TTL toDateTime(occurred_at) + INTERVAL 2 YEAR
SETTINGS index_granularity = 8192;