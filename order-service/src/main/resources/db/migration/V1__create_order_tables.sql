create sequence order_id_seq start with 1 increment by 50;
create sequence customer_id_seq start with 1 increment by 50;
create sequence order_item_id_seq start with 1 increment by 50;

create table customers
(
    id                        bigint default nextval('customer_id_seq') not null unique,
    customer_name             text not null,
    customer_email            text not null,
    customer_phone            text not null,
    delivery_address_line1    text not null,
    delivery_address_line2    text,
    delivery_address_city     text not null,
    delivery_address_state    text not null,
    delivery_address_zip_code text not null,
    delivery_address_country  text not null
);

create table orders
(
    id                        bigint default nextval('order_id_seq') not null unique,
    order_number              text not null unique,
    username                  text not null,
    comments                  text,
    created_at                timestamp not null,
    updated_at                timestamp,
    status                    text not null,
    customer_id bigint not null references customers (id),
    UNIQUE (order_number,customer_id),
    primary key (id)
);

create table order_items
(
    id       bigint default nextval('order_item_id_seq') not null,
    code     text not null,
    name     text not null,
    price    numeric not null,
    quantity integer not null,
    primary key (id),
    order_id bigint  not null references orders (id)
);