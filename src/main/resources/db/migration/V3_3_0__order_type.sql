create table order_type
(
    id          integer primary key generated always as IDENTITY,
    code        integer unique not null,
    description varchar(255),
    created     timestamp default current_timestamp
);