create table eng_order
(
    id            integer primary key generated always as IDENTITY,
    item_code     integer,
    category_code integer,
    name          varchar(255) not null,
    role          varchar(255) not null,
    phone         varchar(255) not null,
    address       varchar(255),
    notes         varchar(255),
    created       timestamp default current_timestamp,
    constraint fk_item_code FOREIGN KEY (item_code) references item (code),
    constraint fk_category_code FOREIGN KEY (category_code) references category (code)
);