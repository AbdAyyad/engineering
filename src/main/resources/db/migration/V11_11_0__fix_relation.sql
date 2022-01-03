alter table eng_order
    rename item_code to item_id;
alter table eng_order
    drop constraint fk_item_code;
alter table eng_order
    add constraint fk_item_id foreign key (item_id) references item (id);
alter table eng_order
    rename category_code to category_id;
alter table eng_order
    drop constraint fk_category_code;
alter table eng_order
    add constraint fk_category_id foreign key (category_id) references category (id);