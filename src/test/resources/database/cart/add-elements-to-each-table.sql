insert into users (id, email, password, first_name, last_name, shipping_address, is_deleted)
values (2, 'email','password','first_name', 'last_name', 'address', 0);
insert into shopping_carts (id, is_deleted) values (2,0);
insert into books (id, title, author, isbn, price, description, cover_image, is_deleted)
values (1,'Kobzar', 'Taras', '1331421', 100, 'book kobzar', 'coverImage', 0);
insert into cart_items (id, book_id, shopping_cart_id, quantity)
values (1, 1, 2, 3);