CREATE TABLE IF NOT EXISTS urls(
    id text,
    url text,
    short_code text,
    expiry_date timestamp default null
)