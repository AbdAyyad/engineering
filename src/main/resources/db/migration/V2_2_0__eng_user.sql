create table eng_user
(
    id        integer primary key generated always as IDENTITY,
    user_name varchar(255) unique not null,
    password  varchar(16)         not null,
    created   timestamp           not null default current_timestamp,
    privilege integer             not null default 1
);