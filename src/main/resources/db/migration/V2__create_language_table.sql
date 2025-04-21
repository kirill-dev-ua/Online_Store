create table language
(
    id             bigserial,
    name           varchar(255),
    created_on     timestamp default CURRENT_TIMESTAMP ,
    last_update_on timestamp default CURRENT_TIMESTAMP,
    constraint pk_language primary key (id),
    constraint language_name_unique unique (name)
);