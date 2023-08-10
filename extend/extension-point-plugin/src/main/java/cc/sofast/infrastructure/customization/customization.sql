create table customization
(
    id     bigserial primary key,
    tenant text,
    key    text,
    val    jsonb
);

create index customization_key_idx on customization (key);

