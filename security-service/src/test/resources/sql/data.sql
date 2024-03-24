INSERT INTO customers (customer_id, customerRole, email, password)
VALUES (1, 'CUSTOMER', 'eva@gmail.com', '$2a$12$npggcybq6606zDlX5XpkAuvFUnnXeG6sQKALWfTtvtLBsRTs.cFTG'),
       (2, 'CUSTOMER', 'second@gmail.com', '$2a$12$npggcybq6606zDlX5XpkAuvFUnnXeG6sQKALWfTtvtLBsRTs.cFTG');
SELECT SETVAL('customers_id_seq', (SELECT MAX(customer_id) FROM customers));