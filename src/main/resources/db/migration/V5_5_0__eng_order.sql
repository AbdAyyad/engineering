create table eng_order
(
    id          integer primary key generated always as IDENTITY,
    sub_item_id integer,
    name        varchar(255) not null,
    role        varchar(255) not null,
    phone       varchar(255) not null,
    address     varchar(255),
    notes       varchar(255),
    created     timestamp default current_timestamp,
    constraint fk_sub_item_id FOREIGN KEY (sub_item_id) references sub_item (id)
);