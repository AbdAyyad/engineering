create table category
(
    id      integer primary key generated always as IDENTITY,
    code    integer unique not null,
    name    varchar(255),
    created timestamp default current_timestamp
);