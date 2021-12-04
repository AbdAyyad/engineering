create table item
(
    id          integer primary key generated always as IDENTITY,
    type_code   integer,
    description varchar(255),
    code        integer unique not null,
    created     timestamp default current_timestamp,
    constraint fk_type_code FOREIGN KEY (type_code) references order_type (code)
);