create table sub_item
(
    id          integer primary key generated always as IDENTITY,
    item_id     integer,
    description varchar(255),
    created     timestamp default current_timestamp,
    constraint fk_item_id FOREIGN KEY (item_id) references item (id)
);