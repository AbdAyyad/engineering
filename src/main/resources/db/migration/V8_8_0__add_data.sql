insert into category (code, name)
values (1, 'supplier');
insert into category (code, name)
values (2, 'contractor');
insert into order_type (code, description)
values (1, 'civil');
insert into order_type (code, description)
values (2, 'mechanic');
insert into order_type (code, description)
values (3, 'electric');
insert into item (code, description, type_code)
values (1, 'concrete', 1);
insert into item (code, description, type_code)
values (2, 'brick', 1);
insert into item (code, description, type_code)
values (3, 'cold-water', 2);
insert into item (code, description, type_code)
values (4, 'firefight', 2);
insert into item (code, description, type_code)
values (5, 'cable', 3);
insert into item (code, description, type_code)
values (6, 'fire-alarm', 3);