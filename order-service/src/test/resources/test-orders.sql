truncate table customers CASCADE;
-- to remove conflict with id for future inserts
alter sequence customer_id_seq restart with 100;
alter sequence order_id_seq restart with 100;
alter sequence order_item_id_seq restart with 1000;

INSERT INTO public.customers
(id, customer_name, customer_email, customer_phone, delivery_address_line1, delivery_address_line2, delivery_address_city, delivery_address_state, delivery_address_zip_code, delivery_address_country)
VALUES (1, 'TestUser1', 'TestUser1@test.com', '647-800-0900', '159 Aph st', '', 'Toronto', 'ON', 'M4S1C0', 'Canada');

INSERT INTO public.orders
(id, order_number, username, "comments", created_at, updated_at, status, customer_id)
VALUES (1, 'ORD-100', 'alex', null, '2024-05-06T12:14:33.142743', null, 'NEW', 1),
       (2, 'ORD-101', 'alex', null, '2024-05-07T14:14:33.142743', null, 'NEW', 1);

INSERT INTO public.order_items
(id, code, "name", price, quantity, order_id)
VALUES (100, 'P100', 'Item1', 10.02, 2, 1),
       (101, 'P101', 'Item2', 21, 5, 1),
       (102, 'P100', 'Item1', 10.02, 2, 2);