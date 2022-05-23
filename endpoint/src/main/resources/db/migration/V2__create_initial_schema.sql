create table webshop
(
    id                  serial
        constraint webshop_pk
            primary key,
    url                 varchar                                            not null,
    handle              varchar                                            not null,
    interest_rate       integer                  default 20                not null,
    created_at          timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_at          timestamp with time zone default now(),
    deleted_at          timestamp with time zone,
    emails              text[],
    service_level_cat_a integer                  default 10                not null,
    service_level_cat_b integer                  default 10                not null,
    service_level_cat_c integer                  default 10                not null
);

alter table webshop
    owner to postgres;

create unique index webshop_handle_uindex
    on webshop (handle);

create function trigger_set_timestamp() returns trigger
    language plpgsql
        as
        $$
        BEGIN
          NEW.updated_at = NOW();
        RETURN NEW;
        END;
        $$;

alter function trigger_set_timestamp() owner to postgres;

create trigger set_timestamp
    before update
    on webshop
    for each row
    execute procedure trigger_set_timestamp();



create table webshop_confg
(
    id             serial
        constraint webshop_confg_pk
            primary key,
    multi_supplier boolean default false,
    run_jobs       boolean default true,
    currency       varchar default 'EUR'::character varying not null,
    fk_webshop     integer                                  not null
        constraint fk_webshop_id
            references webshop
);

alter table webshop_confg
    owner to postgres;

create unique index webshop_confg_fk_webshop_uindex
    on webshop_confg (fk_webshop);


-- only serves as an example, remove this when using this template
-- create table webshop
-- (
--     id        serial
--         constraint webshop_pk
--             primary key,
--     url       varchar            not null,
--     handle    varchar            not null,
--     lvl_one   integer default 20 not null,
--     lvl_two   integer default 20 not null,
--     lvl_three integer default 20 not null,
--     contato   varchar            not null
-- );
--
-- create table webshop_confg
-- (
--     id             serial
--         constraint webshop_confg_pk
--             primary key,
--     multi_supplier boolean default false not null,
--     run_jobs       boolean default true,
--     currency       varchar default 'EUR'::character varying,
--     fk_webshop     integer
--         constraint fk_webshop_id
--             references webshop
-- );