-- Схема для хранения товаров и характеристик товаров специфических для каждой
-- отдельно взятой категории.

-- В схеме должна быть возможность хранения категорий у каждой из которых может
-- быть свой перечень характеристик, например категория `Процессоры` с
-- характеристиками `Производитель`, `Количество ядер`, `Сокет` или категория
-- `Мониторы` с характеристиками `Производитель`, `Диагональ`, `Матрица`,
-- `Разрешение`.

-- Процессоры      -> Intel Core I9 9900 -> AMD Ryzen R7 7700
-- Производитель   -> Intel              -> AMD
-- Количество ядер -> 8                  -> 12
-- Сокет           -> 1250               -> AM4

-- Мониторы      -> Samsung SU556270 -> AOC Z215S659
-- Производитель -> Samsung          -> AOC
-- Диагональ     -> 27               -> 21.5
-- Матрица       -> TN               -> AH-IPS
-- Разрешение    -> 2560*1440        -> 1920*1080
drop table categories;
drop table products;
drop table properties;
drop table prop_values;
drop table feedbacks;
drop table baskets;
drop table users;


create table categories
(
    id            serial8 primary key,
    category_name varchar(20) not null,
    unique (category_name)
);
insert into categories (category_name)
values ('Процессоры'),
       ('Мониторы');

create table products
(
    id           serial8 primary key,
    category_id  int8        not null,
    product_name varchar(50) not null,
    price        int4        not null,
    foreign key (category_id) references categories (id),
    unique (product_name)
);
insert into products (product_name, category_id, price)
values ('Intel Core I9 9900', 1, 250000),
       ('AMD Ryzen R7 7700', 1, 180000),
       ('Samsung SU556270', 2, 90000),
       ('AOC Z215S659', 2, 130000);

create table properties
(
    id            serial8 primary key,
    property_name varchar(30) not null,
    category      int8        not null,
    foreign key (category) references categories (id),
    unique (category, property_name)
);
insert into properties (property_name, category)
values ('Производитель', 1),   -- 1
       ('Количество ядер', 1), -- 2
       ('Сокет', 1); -- 3
insert into properties (property_name, category)
values ('Производитель', 2), -- 4
       ('Диагональ', 2),     -- 5
       ('Матрица', 2),       -- 6
       ('Разрешение', 2); -- 7

create table prop_values
(
    id             serial8 primary key,
    property_value varchar(30) not null,
    property       int8        not null,
    product        int8        not null,
    foreign key (property) references properties (id),
    foreign key (product) references products (id),
    unique (product, property)
);

insert into prop_values (property_value, property, product)
values ('Intel', 1, 1),
       ('8', 2, 1),
       ('1250', 3, 1);

insert into prop_values (property_value, property, product)
values ('AMD', 1, 2),
       ('12', 2, 2),
       ('AM4', 3, 2);

insert into prop_values (property_value, property, product)
values ('Samsung', 4, 3),
       ('27', 5, 3),
       ('TN', 6, 3),
       ('2560*1440', 7, 3);

insert into prop_values (property_value, property, product)
values ('AOC', 4, 4),
       ('21.5', 5, 4),
       ('AH-IPS', 6, 4),
       ('1920*1080', 7, 4);

create table feedbacks
(
    id serial8 primary key,
    user_id int8 not null,
    product_id int8 not null,
    release_status boolean not null default false,
    mark int4 not null check ( mark > 0 and mark < 6 ),
    content varchar(300),
    foreign key (product_id) references products (id),
    foreign key (user_id) references users (id),
    unique (user_id, product_id)
);

create table users (
    id serial8 primary key,
    first_name varchar(30) not null,
    last_name varchar(30) not null,
    login varchar(30) not null,
    password varchar(150) not null,
    phone varchar(17) not null,
    role int2 not null
);

insert into users (first_name, last_name, login, password, phone, role)
VALUES ('Damir', 'Kadyrzhanov', 'damirk120404@gmail.com', '123456', '+77777777777', 1);
insert into users (first_name, last_name, login, password, phone, role)
values ('Admin', 'Admin', 'admin', 'admin', '+77777777777', 0);

create table baskets(
    id serial8 primary key,
    user_id int8 not null,
    product_id int8 not null,
    amount int4 not null default 1,
    foreign key (user_id) references users (id),
    foreign key (product_id) references products (id),
    unique (user_id, product_id)
);