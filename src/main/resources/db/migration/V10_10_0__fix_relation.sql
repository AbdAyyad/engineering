alter table item
    rename type_code to type_id;
alter table item
    drop constraint fk_type_code;
alter table item
    add constraint fk_type_id foreign key (type_id) references order_type (id);