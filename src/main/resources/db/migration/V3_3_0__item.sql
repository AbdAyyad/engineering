create table item
(
    id          integer primary key generated always as IDENTITY,
    serial_id   integer,
    description varchar(255),
    created     timestamp default current_timestamp,
    constraint fk_serial_id FOREIGN KEY (serial_id) references item_serial (id)
);