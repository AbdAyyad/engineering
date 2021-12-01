create table sub_item
(
    id          integer primary key generated always as IDENTITY,
    item_id     integer,
    description varchar(255),
    code        varchar(4) unique not null,
    created     timestamp default current_timestamp,
    constraint fk_item_id FOREIGN KEY (item_id) references item (id)
);