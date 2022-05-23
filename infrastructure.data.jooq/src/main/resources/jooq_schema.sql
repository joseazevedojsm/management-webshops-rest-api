-- PLACE YOUR DATABASE SCHEMA HERE FOR CODE GENERATION

create table webshop
(
    id                 serial
        constraint webshop_pk
            primary key not null,
    url                varchar                                            not null,
    handle             varchar                                            not null,
    emails             text[],
    service_level_cat_a integer                  default 10                not null,
    service_level_cat_b integer                  default 10                not null,
    service_level_cat_c integer                  default 10                not null,
    interest_rate     integer                  default 20                not null,
    created_at         timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_at         timestamp with time zone default now(),
    deleted_at         timestamp with time zone

);
-- default ARRAY []::text[]
create table webshop_confg
(
    id             serial
        constraint webshop_confg_pk
            primary key,
    multi_supplier boolean not null default false,
    run_jobs       boolean not null default true,
    currency       varchar not null default 'EUR',
    fk_webshop     integer not null
        constraint fk_webshop_id
            references webshop
);

