create table category
(
    id             bigserial,
    name           varchar(255),
    created_on     timestamp default CURRENT_TIMESTAMP ,
    last_update_on timestamp default CURRENT_TIMESTAMP,
    constraint pk_category primary key (id),
    constraint category_name_unique unique (name)
);