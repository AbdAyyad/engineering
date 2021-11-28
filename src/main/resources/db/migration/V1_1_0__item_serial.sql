create table item_serial
(
    id          integer primary key generated always as IDENTITY,
    code        varchar(4) unique not null,
    description varchar(255),
    created     timestamp default current_timestamp
);